package cn.ihoway.dao;

import cn.ihoway.entity.Comment;

import java.util.List;

public interface CommentDao {
    int deleteByPrimaryKey(Integer id);

    int insert(Comment record);

    int insertSelective(Comment record);

    Comment selectByPrimaryKey(Integer id);

    Comment selectByIdAndType(Integer id,Integer type);

    //查询文章下面全部一级评论或者一级评论下全部二级评论
    List<Comment> selectCommentsByBelongId(Integer belongId, Integer type,Integer index,Integer size);

    //查询文章下全部一级评论或者一级评论全部二级评论的数量
    int selectFloorNumsByBelongId(Integer belongId, Integer type);

    int updateByPrimaryKeySelective(Comment record);

    int updateByPrimaryKey(Comment record);
}