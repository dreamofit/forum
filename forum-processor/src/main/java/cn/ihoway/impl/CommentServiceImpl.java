package cn.ihoway.impl;

import cn.ihoway.dao.CommentDao;
import cn.ihoway.dao.EssayDao;
import cn.ihoway.entity.Comment;
import cn.ihoway.service.CommentService;
import cn.ihoway.util.MybatisUtils;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

public class CommentServiceImpl implements CommentService {
    private final SqlSession sqlSession = MybatisUtils.getSqlSession();
    private final EssayDao essayDao = sqlSession.getMapper(EssayDao.class);
    private final CommentDao commentDao = sqlSession.getMapper(CommentDao.class);

    /**
     * 插入一级评论
     * @param comment
     * @return
     */
    @Override
    public int addFloorComment(Comment comment) {
        //查询文章id是否存在
        if(essayDao.selectByPrimaryKey(comment.getBelongId()) != null){
            return writeComment(comment);
        }
        return -1;
    }

    /**
     * 插入二级评论
     * @param comment
     * @return
     */
    @Override
    public int addLayerComment(Comment comment) {
        //查询一级评论和被回复是否存在
        if(commentDao.selectByPrimaryKey(comment.getBelongId()) != null){
            if(comment.getBelongId().equals(comment.getRepliedId()) || commentDao.selectByIdAndType(comment.getRepliedId(),2) != null){
                return writeComment(comment);
            }

        }
        return -1;
    }

    @Override
    public List<Comment> findCommentsByBelongId(Integer belongId,Integer type,Integer index,Integer size) {
        return commentDao.selectCommentsByBelongId(belongId,type,index,size);
    }

    @Override
    public int findCommentNumByBelongId(Integer belongId, Integer type) {
        return commentDao.selectFloorNumsByBelongId(belongId,type);
    }

    @Override
    public Comment findCommentById(Integer id) {
        return commentDao.selectByPrimaryKey(id);
    }

    private int writeComment(Comment comment) {
        //查询所属文章或评论已有评论数
        int floor = commentDao.selectFloorNumsByBelongId(comment.getBelongId(), comment.getType()) + 1;
        comment.setFloor(floor);
        int rs = commentDao.insertSelective(comment);
        if (rs > 0) {
            sqlSession.commit();

        }else{
            sqlSession.rollback();
        }
        return rs;
    }
    @Override
    public void free(){
        sqlSession.close();
    }

}
