package cn.ihoway.dao;

import cn.ihoway.entity.Operate;

import java.util.List;

public interface OperateDao {
    int deleteByPrimaryKey(Integer id);

    int insert(Operate record);

    int insertSelective(Operate record);

    Operate selectByPrimaryKey(Integer id);

    Operate selectByIndex(Integer userId,Integer type,Integer optId,Integer action);

    int updateByPrimaryKeySelective(Operate record);

    int updateByIndex(Operate record);

    int updateByPrimaryKey(Operate record);

    //List<Operate> selectGroupByType();
    List<Operate> selectALl();

    int truncate();
}