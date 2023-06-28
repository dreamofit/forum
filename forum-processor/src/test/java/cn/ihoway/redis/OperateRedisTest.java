package cn.ihoway.redis;

import cn.ihoway.entity.Operate;
import cn.ihoway.util.HowayRedisCache;
import org.junit.jupiter.api.Test;
import org.mybatis.caches.redis.SerializeUtil;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class OperateRedisTest {

    OperateRedis operateRedis = new OperateRedis();

    @Test
    void init() {
        operateRedis.init();
        Operate operate = new Operate();
        operate.setUserId(1);
        operate.setType(0);
        operate.setOptId(5);
        operate.setAction(2);
        operate.setUpdateTime("2022-03-13");
        operateRedis.add(operate);
        HowayRedisCache cache = new HowayRedisCache("Operate.support");
        Map<byte[], byte[]> result = cache.getAll();
        for(byte[] key:result.keySet()){
            System.out.println(new String(key));
            System.out.println(SerializeUtil.unserialize(result.get(key)));
        }
    }

    @Test
    void test() {
        //operateRedis.free();
        //operateRedis.init();
        List<Operate> operates = operateRedis.getConditionMatch(null,null,null,null);
        for(Operate operate:operates){
            System.out.println(operate.toString());
        }
    }

    @Test
    void tes2(){
        //operateRedis.free();
        HowayRedisCache cache = new HowayRedisCache("cn.ihoway.entity.Operate");
        Map<byte[], byte[]> result = cache.getAll();
        for(byte[] key:result.keySet()){
            System.out.println(new String(key));
            System.out.println(SerializeUtil.unserialize(result.get(key)));
        }
    }


}