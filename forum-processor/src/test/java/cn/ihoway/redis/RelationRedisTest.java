package cn.ihoway.redis;

import cn.ihoway.entity.Relation;
import cn.ihoway.util.HowayRedisCache;
import org.junit.jupiter.api.Test;
import org.mybatis.caches.redis.SerializeUtil;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class RelationRedisTest {

    RelationRedis relationRedis = new RelationRedis();

    @Test
    void add() {
        HowayRedisCache cache = new HowayRedisCache("cn.ihoway.entity.Relation");
        //cache.clear();
        Map<byte[], byte[]> result = cache.getAll();
        for(byte[] key:result.keySet()){
            System.out.println(new String(key));
            System.out.println(SerializeUtil.unserialize(result.get(key)));
        }
    }

    @Test
    void test() {
        List<Relation> relations = relationRedis.getConditionMatch(null,2,null,null);
        for(Relation relation:relations){
            System.out.println(relation.toString());
        }
    }
}