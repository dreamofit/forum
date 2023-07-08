package cn.ihoway.impl;

import cn.ihoway.dao.OperateDao;
import cn.ihoway.entity.Operate;
import cn.ihoway.service.OperateService;
import cn.ihoway.util.MybatisUtils;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OperateServiceImpl implements OperateService {

    private final SqlSession sqlSession = MybatisUtils.getSqlSession();
    private final OperateDao operateDao = sqlSession.getMapper(OperateDao.class);


    @Override
    public int add(Operate record) {
        int rs = operateDao.insertSelective(record);
        if (rs > 0) {
            sqlSession.commit();

        }else{
            sqlSession.rollback();
        }
        return rs;
    }

    @Override
    public int update(Operate record){
        int rs = operateDao.updateByIndex(record);
        if (rs > 0) {
            sqlSession.commit();

        }else{
            sqlSession.rollback();
        }
        return rs;
    }

    @Override
    public List<Operate> selectAll() {
        return operateDao.selectALl();
    }

    @Override
    public Operate selectOne(Integer userId,Integer type,Integer optId,Integer action) {
        return operateDao.selectByIndex(userId,type,optId,action);
    }

    @Override
    public int truncate(){
        return operateDao.truncate();
    }

    @Override
    public void free(){
        sqlSession.close();
    }

}
