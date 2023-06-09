package stu.imzjm.service;

import com.github.pagehelper.PageInfo;
import stu.imzjm.model.domain.Article;


import java.util.List;

public interface IArticleService {
    //分页查询文章列表
    PageInfo<Article> selectArticleWithPage(Integer page, Integer count);

    //统计热度前十的文章信息
    List<Article> getHeatArticles();

    //根据文章id查询单个文章详情
    Article selectArticleWithId(Integer id);

    /**
     * 发布文章
     */
    void publish(Article article);

    /**
     * 根据 文章id 修改文章
     */
    void updateArticleWithId(Article article);
}
