package stu.imzjm.dao;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import stu.imzjm.model.domain.Comment;

import java.util.List;

/**
 * 文章评论操作 对应表t_comment
 * <pre>id      article_id      created     ip      content     status      author</pre>
 * <pre>id      文章id           评论时间     ip      内容         状态        评论者</pre>
 */
@Mapper
public interface CommentMapper {
    /**
     * 分页展示某个文章的评论
     */
    @Select("select * from t_comment where article_id = #{articleId} order by id desc")
    public List<Comment> selectCommentWithPage(Integer articleId);

    /**
     * 查询最新几条评论
     */
    @Select("select * from t_comment order by id desc")
    public List<Comment> selectNewComment();

    /**
     * 发表评论
     */
    @Insert("insert into t_comment (article_id, created, ip, content, author) " +
            "VALUES (#{articleId}, #{created}, #{ip}, #{content}, #{author})")
    public void pushComment(Comment comment);

    /**
     * 统计评论数量
     */
    @Select("select count(1) from t_comment")
    public Integer countComment();

    /**
     * 通过文章id删除评论
     *
     * @param articleId 文章id
     */
    @Delete("delete from t_comment where article_id = #{articleId}")
    public void deleteCommentWithId(Integer articleId);
}
