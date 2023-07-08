package cn.ihoway.impl;

import cn.ihoway.dao.RelationDao;
import cn.ihoway.entity.Relation;
import cn.ihoway.service.RelationService;
import cn.ihoway.util.MybatisUtils;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

public class RelationServiceImpl implements RelationService {

    private final SqlSession sqlSession = MybatisUtils.getSqlSession();
    private final RelationDao relationDao = sqlSession.getMapper(RelationDao.class);

    @Override
    public int add(Relation record) {
        int rs = relationDao.insertSelective(record);
        if (rs > 0) {
            sqlSession.commit();

        }else{
            sqlSession.rollback();
        }
        return rs;
    }

    @Override
    public int truncate() {
        return relationDao.truncate();
    }

    @Override
    public List<Relation> selectAll() {
        return relationDao.selectALl();
    }

    @Override
    public void free(){
        sqlSession.close();
    }
}
