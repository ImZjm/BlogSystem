package stu.imzjm.service;

import com.github.pagehelper.PageInfo;
import stu.imzjm.model.domain.Comment;

/**
 * 文章评论业务处理
 */
public interface ICommentService {
    /**
     * 根据 文章id 分页查询评论
     */
    public PageInfo<Comment> getComments(Integer articleId, Integer page, Integer count);

    /**
     * 发表评论
     */
    public void pushComment(Comment comment);
}
