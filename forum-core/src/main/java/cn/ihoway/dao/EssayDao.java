package cn.ihoway.dao;

import cn.ihoway.entity.Essay;

import java.util.List;

public interface EssayDao {
    int deleteByPrimaryKey(Integer id);

    int insert(Essay record);

    int insertSelective(Essay record);

    Essay selectByPrimaryKey(Integer id);

    List<Essay> selectALLByType(Integer type,Integer index,Integer size);

    List<Essay> selectByUser(Integer type,Integer userId,Integer index,Integer size);

    int updateByPrimaryKeySelective(Essay record);

    int updateByPrimaryKey(Essay record);
}