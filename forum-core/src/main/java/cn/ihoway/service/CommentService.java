package cn.ihoway.service;

import cn.ihoway.entity.Comment;

import java.util.List;

public interface CommentService {
    //写一级评论，需要查询文章是否存在
    int addFloorComment(Comment comment);
    //写二级评论，需要查询文章是否存在和一级评论是否存在，一级被回复的评论是否存在
    int addLayerComment(Comment comment);
    //根据类型和所属Id查询指定范围内的全部评论
    List<Comment> findCommentsByBelongId(Integer belongId,Integer type,Integer index,Integer size);
    //根据类型和所属id查询全部评论的数量
    int findCommentNumByBelongId(Integer belongId,Integer type);
    Comment findCommentById(Integer id);
    void free();
}
