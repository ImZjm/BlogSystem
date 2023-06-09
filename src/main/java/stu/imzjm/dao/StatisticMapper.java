package stu.imzjm.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import stu.imzjm.model.domain.Article;
import stu.imzjm.model.domain.Statistic;

import java.util.List;

/**
 * 文章统计资料 对应数据表 t_statistic
 * <hr/>
 * <pre>id   article_id   hits(点击量)    comments_num(评论量)</pre>
 */
@Mapper
public interface StatisticMapper {
    /**
     * 新增一条统计数据
     */
    @Insert("insert into t_statistic(article_id, hits, comments_num) values (#{id},0,0)")
    public void addStatistic(Article article);

    /**
     * 根据<u color="red">文章id</u>查询点击量和评论量
     */
    @Select("select * from t_statistic where article_id = #{articleId}")
    public Statistic selectStatisticWithArticleId(Integer articleId);

    /**
     * 根据 article_id 更新点击量
     */
    @Update("update t_statistic set hits = #{hits} where article_id = #{articleId}")
    public void updateArticleHitsWithId(Statistic statistic);

    /**
     * 根据 article_id 更新评论量
     */
    @Update("update t_statistic set comments_num = #{commentsNum} where article_id = #{articleId}")
    public void updateArticleCommentsWithId(Statistic statistic);

    /**
     * 根据 article_id 删除统计数据
     */
    @Update("delete from t_statistic where article_id = #{article_id}")
    public void deleteStatisticWithId(Integer article_id);

    /**
     * 获取文章热度信息
     */
    @Select("select * from t_statistic where hits != '0' order by hits desc, comments_num desc")
    public List<Statistic> getStatistic();

    /**
     * 统计博客 文章 总访问量
     */
    @Select("select sum(hits) from t_statistic")
    public long getTotalVisit();

    /**
     * @return 博客文章总评论量
     */
    @Select("select sum(comments_num) from t_statistic")
    public long getTotalComment();

}
