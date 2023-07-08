package cn.ihoway.service;

import cn.ihoway.entity.Relation;
import org.springframework.stereotype.Service;

import java.util.List;

public interface RelationService {
    int add(Relation record);
    int truncate();
    List<Relation> selectAll();
    public void free();
}
