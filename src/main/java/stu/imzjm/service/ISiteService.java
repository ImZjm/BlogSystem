package stu.imzjm.service;

import stu.imzjm.model.ResponseData.StaticticsBo;
import stu.imzjm.model.domain.Article;
import stu.imzjm.model.domain.Comment;

import java.util.List;

/**
 * 站点统计
 */
public interface ISiteService {
    //最新评论
    public List<Comment> recentComments(Integer count);

    //最新发表的文章
    public List<Article> recentArticles(Integer count);

    //后台统计数据
    public StaticticsBo getStatistics();

    //更新某个文章的统计数据

    /**
     * 点击量 加1
     *
     * @param article 文章实体类
     */
    public void updateStatistics(Article article);
}
