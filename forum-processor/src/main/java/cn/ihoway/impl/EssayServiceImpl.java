package cn.ihoway.impl;

import cn.ihoway.dao.EssayDao;
import cn.ihoway.entity.Essay;
import cn.ihoway.service.EssayService;
import cn.ihoway.util.MybatisUtils;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

public class EssayServiceImpl implements EssayService {
    private final SqlSession sqlSession = MybatisUtils.getSqlSession();
    private final EssayDao essayDao = sqlSession.getMapper(EssayDao.class);
    @Override
    public int addEssay(Essay essay) {
        int rs = essayDao.insertSelective(essay);
        if(rs > 0){
            sqlSession.commit();
        }else {
            sqlSession.rollback();
        }
        return rs;
    }

    @Override
    public Essay findById(int id) {
        return essayDao.selectByPrimaryKey(id);
    }

    @Override
    public List<Essay> findAll(int type, int index, int size) {
        return essayDao.selectALLByType(type,index,size);
    }


    @Override
    public int updateEssay(Essay essay) {
        return 0;
    }

    @Override
    public int deleteEssay(Essay essay) {
        return 0;
    }

    @Override
    public List<Essay> findByUser(int type,int userId, int index, int size) {
        return essayDao.selectByUser(type,userId,index,size);
    }

    @Override
    public void free(){
        sqlSession.close();
    }

}
