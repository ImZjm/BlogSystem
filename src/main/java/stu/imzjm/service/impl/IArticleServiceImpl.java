package stu.imzjm.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.vdurmont.emoji.EmojiParser;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import stu.imzjm.dao.ArticleMapper;
import stu.imzjm.dao.CommentMapper;
import stu.imzjm.dao.StatisticMapper;
import stu.imzjm.model.domain.Article;
import stu.imzjm.model.domain.Statistic;
import stu.imzjm.service.IArticleService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class IArticleServiceImpl implements IArticleService {
    @Resource
    private ArticleMapper articleMapper;

    @Resource
    private StatisticMapper statisticMapper;

    @Resource
    private RedisTemplate<String, Article> redisTemplate;

    @Resource
    private CommentMapper commentMapper;

    //分页查询文章列表
    @Override
    public PageInfo<Article> selectArticleWithPage(Integer page, Integer count) {
        PageHelper.startPage(page, count);
        List<Article> articleList = articleMapper.selectArticleWithPage();
        for (Article article :
                articleList) {
            Statistic statistic = statisticMapper.selectStatisticWithArticleId(article.getId());
            //将查询到的文章统计数据写入到 Article类 中
            article.setHits(statistic.getHits());
            article.setCommentsNum(statistic.getCommentsNum());
        }
        return new PageInfo<>(articleList);
    }

    //统计热度前十的文章
    @Override
    public List<Article> getHeatArticles() {
        List<Statistic> statisticList = statisticMapper.getStatistic();
        List<Article> articleList = new ArrayList<>();
        for (Statistic s :
                statisticList) {
            Article article = articleMapper.selectArticleWithId(s.getArticleId());
            article.setHits(s.getHits());
            article.setCommentsNum(s.getCommentsNum());
            articleList.add(article);

            //当已经有10条数据时, 跳出
            if (articleList.size() >= 10) break;
        }
        return articleList;
    }

    //根据文章id查询单个文章详情
    //并使用 redis缓存
    @Override
    public Article selectArticleWithId(Integer id) {
        Article article;
        //从缓存中读取 Article
        Article articleFromCache = redisTemplate.opsForValue().get("article_" + id);
        if (articleFromCache != null)
            article = articleFromCache;
        else if ((article = articleMapper.selectArticleWithId(id)) != null)
            redisTemplate.opsForValue().set("article_" + id, article);
        return article;
    }

    @Override
    public void publish(Article article) {
        //去除 标题 表情
        article.setTitle(EmojiParser.parseToAliases(article.getTitle()));
        //去除表情
        article.setContent(EmojiParser.parseToAliases(article.getContent()));
        article.setCreated(new Date());
        article.setHits(0);
        article.setCommentsNum(0);

        //更新到文章表 和 文章统计表
        articleMapper.publishArticle(article);
        statisticMapper.addStatistic(article);
    }

    @Override
    public void updateArticleWithId(Article article) {
        //去除 标题 表情
        article.setTitle(EmojiParser.parseToAliases(article.getTitle()));
        //去除表情
        article.setContent(EmojiParser.parseToAliases(article.getContent()));
        article.setModified(new Date());
        articleMapper.updateArticleWithId(article);
        redisTemplate.delete("article_" + article.getId());
    }

    //删除文章
    @Override
    public void deleteArticleWithId(int id) {
        //删除文章,同时删除文章的缓存
        articleMapper.deleteArticleWithId(id);
        redisTemplate.delete("article_" + id);

        //删除统计数据
        statisticMapper.deleteStatisticWithId(id);

        //删除评论数据
        commentMapper.deleteCommentWithId(id);
    }
}
