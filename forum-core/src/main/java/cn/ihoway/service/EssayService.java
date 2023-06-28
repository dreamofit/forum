package cn.ihoway.service;

import cn.ihoway.entity.Essay;

import java.util.List;

public interface EssayService {
    //1.发表文章
    int addEssay(Essay essay);
    //2.查询某个文章
    Essay findById(int id);
    //3.分页查询某类型所有文章
    List<Essay> findAll(int type,int index,int size);
    //4.更新文章
    int updateEssay(Essay essay);
    //5.删除文章
    int deleteEssay(Essay essay);
    //6.通过标签查询文章
    //7.通过话题查询文章
    //8.通过用户id查询其发表的全部文章
    List<Essay> findByUser(int type,int userId,int index,int size);
    void free();
}
