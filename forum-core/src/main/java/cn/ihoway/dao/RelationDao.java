package cn.ihoway.dao;

import cn.ihoway.entity.Relation;

import java.util.List;

public interface RelationDao {
    int deleteByPrimaryKey(Integer id);

    int insert(Relation record);

    int insertSelective(Relation record);

    Relation selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Relation record);

    int updateByPrimaryKey(Relation record);

    int truncate();

    List<Relation> selectALl();
}