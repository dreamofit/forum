package cn.ihoway.service;

import cn.ihoway.entity.Operate;

import java.util.List;

public interface OperateService {
    //新增普通行为
    int add(Operate record);
    int update(Operate record);
    //查询全部
    List<Operate> selectAll();
    Operate selectOne(Integer userId,Integer type,Integer optId,Integer action);
    int truncate();
}
