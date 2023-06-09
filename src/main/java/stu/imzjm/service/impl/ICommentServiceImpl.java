package stu.imzjm.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import stu.imzjm.dao.CommentMapper;
import stu.imzjm.dao.StatisticMapper;
import stu.imzjm.model.domain.Comment;
import stu.imzjm.model.domain.Statistic;
import stu.imzjm.service.ICommentService;

import java.util.List;

@Service
@Transactional
public class ICommentServiceImpl implements ICommentService {
    @Resource
    private CommentMapper commentMapper;

    @Resource
    private StatisticMapper statisticMapper;

    @Override
    public PageInfo<Comment> getComments(Integer articleId, Integer page, Integer count) {
        PageHelper.startPage(page, count);
        List<Comment> commentList = commentMapper.selectCommentWithPage(articleId);

        return new PageInfo<>(commentList);
    }

    @Override
    public void pushComment(Comment comment) {
        commentMapper.pushComment(comment);

        //更新文章评论数   评论数 +1
        Statistic statistic = statisticMapper.selectStatisticWithArticleId(comment.getArticleId());
        statistic.setCommentsNum(statistic.getCommentsNum() + 1);
        statisticMapper.updateArticleCommentsWithId(statistic);
    }
}
