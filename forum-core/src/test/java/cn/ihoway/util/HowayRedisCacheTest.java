package cn.ihoway.util;

import com.alibaba.fastjson.JSON;
import org.junit.jupiter.api.Test;
import org.mybatis.caches.redis.SerializeUtil;
import redis.clients.jedis.Jedis;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class HowayRedisCacheTest {

    @Test
    void getId() {
        Jedis jedis = new Jedis();
        System.out.println(jedis.dbSize());
        System.out.println(jedis.keys("*"));
//        HowayRedisCache cache = new HowayRedisCache("h_cache");
//        cache.putObject("mini","1");
//        System.out.println(cache.getObject("mini"));
//        //cache.clear();
//        cache.putObject("mini","2");
//        cache = new HowayRedisCache("h_cache");
//        System.out.println(cache.getObject("mini"));
//        cache.clear();
    }

    @Test
    void getSize() {
        HowayRedisCache cache = new HowayRedisCache("cn.ihoway.dao.EssayDao");
        Map<byte[], byte[]> result = cache.getAll();
        for(byte[] key:result.keySet()){
            System.out.println(new String(key));
            System.out.println(SerializeUtil.unserialize(result.get(key)));
        }
    }

    @Test
    void getObject() {
    }
}