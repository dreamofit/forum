package cn.ihoway.redis;

import cn.ihoway.entity.Relation;
import cn.ihoway.impl.RelationServiceImpl;
import cn.ihoway.service.RelationService;
import cn.ihoway.type.RelationAct;
import cn.ihoway.util.HowayLog;
import cn.ihoway.util.HowayRedisCache;
import org.mybatis.caches.redis.SerializeUtil;

import java.util.List;
import java.util.Map;

/**
 * key:firstId+" "+action+" "+secondId
 * 私信key:firstId+" "+action+" "+secondId+“ ”+num
 */
public class RelationRedis {
    private HowayRedisCache cache = new HowayRedisCache("cn.ihoway.entity.Relation");
    private final HowayLog logger = new HowayLog(RelationRedis.class);
    private final RelationService relationService = new RelationServiceImpl();

    /**
     * 已缓存为主
     */
    public void init(){
        if(cache.getSize() == 0){
            //从数据库读取全部数据
            List<Relation> relations = relationService.selectAll();
            for(Relation relation:relations){
                add(relation);
            }
        }
    }

    //新增一个关系
    public boolean add(Relation relation){
        String key = getKey(relation);
        //私信可以重复，其他不可以
        if(relation.getAction() != RelationAct.CHAT.getType()){
            if(cache.getObject(key) != null){
                return false;
            }
            //反转查询是否存在,存在则删除，在前端进行提示示警
            String newKey = "";
            if(relation.getAction() == RelationAct.FOLLOW.getType()){
                newKey = getKey(relation.getFirstId(),RelationAct.UNFRIEND.name(), relation.getSecondId());
            }else {
                newKey = getKey(relation.getFirstId(),RelationAct.FOLLOW.name(), relation.getSecondId());
            }
            if(cache.getObject(newKey) != null){
                cache.removeObject(newKey);
            }
        }else {
            List<Relation> relationList = getConditionMatch(relation.getFirstId(),relation.getAction(),relation.getSecondId(),null);
            int size = relationList.size() + 1;
            relation.setNum(size);
            key += " " + size;
        }
        cache.putObject(key,relation);
        return true;
    }

    //删除一个关系
    public boolean delete(Relation relation){
        String key = getKey(relation);
        //todo 私信只允许两分钟内撤回
        if(relation.getAction() != RelationAct.CHAT.getType()){
            if(cache.getObject(key) != null){
                cache.removeObject(key);
                return true;
            }
        }
        return false;
    }

    private String getKey(Relation relation){
        if(relation == null){
            return null;
        }
        if(relation.getAction() == RelationAct.CHAT.getType() && relation.getNum() != null){
            return relation.getFirstId() + " " + relation.getAction() + " " + relation.getSecondId() + " " + relation.getNum();
        }
        return relation.getFirstId() + " " + relation.getAction() + " " + relation.getSecondId();
    }

    private String getKey(Integer firstId,String action,Integer secondId){
        return firstId + " " + action + " " + secondId;
    }

    //持久化数据库
    public void putAllToDb(){
        relationService.truncate();
        Map<byte[], byte[]> map = cache.getAll();
        for(byte[] key:map.keySet()){
            Relation relation = (Relation) SerializeUtil.unserialize(map.get(key));
            relationService.add(relation);
        }
        relationService.free();
    }

    /**
     * 按格式取出满足相应条件的全部数据集合,若不关系该条件是否满足则置为null
     * @return list
     */
    public List<Relation> getConditionMatch(Object... arguments){
        return cache.getConditionMatch(" ",arguments);
    }


}
