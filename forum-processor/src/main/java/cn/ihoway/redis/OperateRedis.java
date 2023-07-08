package cn.ihoway.redis;

import cn.ihoway.entity.Operate;
import cn.ihoway.impl.OperateServiceImpl;
import cn.ihoway.service.OperateService;
import cn.ihoway.type.OperateAct;
import cn.ihoway.util.HowayLog;
import cn.ihoway.util.HowayRedisCache;
import org.mybatis.caches.redis.SerializeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class OperateRedis {
    private HowayRedisCache cache  = new HowayRedisCache("cn.ihoway.entity.Operate"); //主键缓存 key为主键 user_id type opt_id action
    private HowayRedisCache supportCache = new HowayRedisCache("Operate.support"); //点赞数量缓存 key为 type opt_id
    private final HowayLog logger = new HowayLog(OperateRedis.class);
    private final OperateService operateService = new OperateServiceImpl();
    private final int INDEX_MODE = 1; //索引模式
    private final int OPT_MODE = 2; //以type+optId为准
    private final int INVERSE_MODE = 3; //反转index key





    /**
     * 已缓存为主
     */
    public void init(){
        //if(cache.getSize() == 0){
            //从数据库读取全部数据
            List<Operate> operates = operateService.selectAll();
            for(Operate operate:operates){
                add(operate);
            }
        //}
        //key 为：唯一索引(user_id,type,opt_id,action)
    }

    /**
     * 新增数据
     * 如果该主键存在走更新分支
     * 否则，新增
     * 其中，新增点赞的时候需要检查user_id,type,opt_id+反对主键是否存在
     *      若存在且该key对应的status也为1，则需要将其更新为0再新增
     *      否则，直接新增
     * 新增时候，需要记得增加点赞数量
     * 其中，新增反对的时候需要检查user_id,type,opt_id+点赞主键是否存在
     *      若存在且该key对应的status为1，则需要将其更新为0，再新增，同时减少点赞数量
     *      否则，直接新增
     * @param operate
     */
    public void add(Operate operate){
        String key = getKey(operate,INDEX_MODE);
        if(cache.getObject(key) != null){
            //走更新分支
            update(operate,key);
            return;
        }
        //理论上新增一条行为为0的数据不需要写入缓存或者数据库中
        if(operate.getStatus() == 0){
            return;
        }
        //如果是点赞类型，点赞数量缓存需要加1
        if(operate.getAction() == OperateAct.SUPPORT.getType()){
            specialUpdate(operate);
            addSupportNum(operate);
        }
        if(operate.getAction() == OperateAct.OPPOSE.getType()){
            specialUpdate(operate);
        }
        cache.putObject(key,operate);
        
    }

    /**
     * 更新数据
     * 如果更新点赞状态为0，需要减少点赞数量
     * 如果更新点赞状态为1，需要看主键user_id,type,opt_id+反对是否存在，存在需要更新为0，同时新增点赞数量
     * 如果更新反对状态为1，看主键ser_id,type,opt_id+点赞是否存在，存在需要更新为0，同时减少点赞数量
     * @param operate
     * @param key
     */
    public void update(Operate operate,String key){
        Operate oldOperate = (Operate) cache.getObject(key);
        logger.info("原对象：" + oldOperate.toString());
        logger.info("更新为：" + operate.toString());
        //新旧状态一样不需要进行修改
        if(operate.getStatus().equals(oldOperate.getStatus())){
            return;
        }
        if(operate.getStatus() == 0){
            if(operate.getAction() == OperateAct.SUPPORT.getType()){
                reduceSupportNum(operate);
            }
        }else {
            //更新点赞状态为1
            if(operate.getAction() == OperateAct.SUPPORT.getType()){
                specialUpdate(operate);
                addSupportNum(operate);
            }
            if(operate.getAction() == OperateAct.OPPOSE.getType()){
                specialUpdate(operate);
            }
        }
        cache.putObject(key,operate);
    }

    /**
     * 点赞数量-1
     * @param operate
     */
    private void reduceSupportNum(Operate operate) {
        String sk = getKey(operate,OPT_MODE);
        Integer num = (Integer) supportCache.getObject(sk);
        if(num == null || num <= 0){
            num = 0;
        }else {
            num--;
        }
        supportCache.putObject(sk,num);
    }

    /**
     * 点赞数量+1
     * @param operate
     */
    private void addSupportNum(Operate operate) {
        String sk = getKey(operate, OPT_MODE);
        Integer num = (Integer) supportCache.getObject(sk);
        if (num == null) {
            num = 1;
        } else {
            num++;
        }
        supportCache.putObject(sk, num);
    }

    /**
     * 反转模式进行更新，将状态为1的改为状态为0
     * @param operate
     */
    private void specialUpdate(Operate operate) {
        String tempKey = getKey(operate, INVERSE_MODE);
        Operate tempOperate = (Operate) cache.getObject(tempKey);
        if (tempOperate != null && tempOperate.getStatus() == 1) {
            tempOperate.setStatus(0);
            update(tempOperate, tempKey);
        }
    }

    /**
     * 
     * @param operate operate对象
     * @param mode INDEX_MODE:主键模式 OPT_MODE：type+opt_id模式  INVERSE_MODE:相反模式（点赞变点踩）
     * @return
     */
    private String getKey(Operate operate,int mode){
        if(operate==null){
            return null;
        }
        if(mode == INDEX_MODE){
            return operate.getUserId() + " " + operate.getType() + " " + operate.getOptId() + " " + operate.getAction();
        }else if(mode == INVERSE_MODE){
            if(operate.getAction() == OperateAct.SUPPORT.getType()){
                return operate.getUserId() + " " + operate.getType() + " " + operate.getOptId() + " " + OperateAct.OPPOSE.getType();
            }
            return operate.getUserId() + " " + operate.getType() + " " + operate.getOptId() + " " + OperateAct.SUPPORT.getType();
        }
        return operate.getType() + " " + operate.getOptId();
    }
    


    //持久化数据库
    public void putAllToDb(){
        operateService.truncate();
        Map<byte[], byte[]> map = cache.getAll();
        for(byte[] key:map.keySet()){
            Operate operate = (Operate) SerializeUtil.unserialize(map.get(key));
            //Operate dbOperate = operateService.selectOne(operate.getUserId(),operate.getType(),operate.getOptId(),operate.getAction());
            //if(dbOperate == null){
            operateService.add(operate);
            //}else if(!operate.equals(dbOperate)){
                //operateService.update(operate);
           // }
        }
        operateService.free();
    }

    /**
     * 按格式取出满足相应条件的全部数据集合,若不关系该条件是否满足则置为null
     * @return list
     */
    public List<Operate> getConditionMatch(Object... arguments){
        return cache.getConditionMatch(" ",arguments);
    }

    /**
     * 同唯一索引取值
     * @param uid 用户id
     * @param type 类型
     * @param optId 被操纵对象id
     * @param action 行为
     * @return Operate结果
     */
    public Operate getByIndex(Integer uid,Integer type,Integer optId,Integer action){
        String key = uid + " " + type + " " + optId + " " + action;
        return (Operate) cache.getObject(key);
    }

    public void free(){
        cache.clear();
        supportCache.clear();
    }




}
