package cn.ihoway.service;

import cn.ihoway.entity.Relation;

import java.util.List;

public interface RelationService {
    int add(Relation record);
    int truncate();
    List<Relation> selectAll();
}
