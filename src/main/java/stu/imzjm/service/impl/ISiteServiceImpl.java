package stu.imzjm.service.impl;

import com.github.pagehelper.PageHelper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import stu.imzjm.dao.ArticleMapper;
import stu.imzjm.dao.CommentMapper;
import stu.imzjm.dao.StatisticMapper;
import stu.imzjm.model.ResponseData.StaticticsBo;
import stu.imzjm.model.domain.Article;
import stu.imzjm.model.domain.Comment;
import stu.imzjm.model.domain.Statistic;
import stu.imzjm.service.ISiteService;

import java.util.List;

@Service
@Transactional
public class ISiteServiceImpl implements ISiteService {
    @Resource
    private CommentMapper commentMapper;

    @Resource
    private ArticleMapper articleMapper;

    @Resource
    private StatisticMapper statisticMapper;

    @Override
    public List<Comment> recentComments(Integer count) {
        PageHelper.startPage(1, count > 10 || count < 1 ? 10 : count);

        return commentMapper.selectNewComment();
    }

    @Override
    public List<Article> recentArticles(Integer count) {
        PageHelper.startPage(1, count > 10 || count < 1 ? 10 : count);
        List<Article> articleList = articleMapper.selectArticleWithPage();
        for (Article article :
                articleList) {
            Statistic statistic = statisticMapper.selectStatisticWithArticleId(article.getId());
            article.setHits(statistic.getHits());
            article.setCommentsNum(statistic.getCommentsNum());
        }
        return articleList;
    }

    @Override
    public StaticticsBo getStatistics() {
        StaticticsBo staticticsBo = new StaticticsBo();
        Integer articles = articleMapper.countArticle();
        Integer comments = commentMapper.countComment();
        staticticsBo.setArticles(articles);
        staticticsBo.setComments(comments);
        return staticticsBo;
    }

    @Override
    public void updateStatistics(Article article) {
        Statistic statistic = statisticMapper.selectStatisticWithArticleId(article.getId());
        //点击量 加 1
        statistic.setHits(statistic.getHits() + 1);
        statisticMapper.updateArticleHitsWithId(statistic);
    }
}
