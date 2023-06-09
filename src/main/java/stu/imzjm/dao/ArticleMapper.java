package stu.imzjm.dao;

import org.apache.ibatis.annotations.*;
import stu.imzjm.model.domain.Article;

import java.util.List;

/**
 * 文章操作 对应数据表 t_article
 */
@Mapper
public interface ArticleMapper {

    /**
     * 根据 id 查询文章信
     */
    @Select("select * from t_article where id = #{id}")
    public Article selectArticleWithId(Integer id);

    /**
     * 发表文章
     */
    @Insert("insert into t_article (title, content, created, modified, categories, tags, allow_comment, thumbnail) " +
            "values (#{title}, #{content}, #{created}, #{modified}, #{categories}, #{tags}, #{allowComment}, #{thumbnail})")
    //同时使用 @Options 注解获取自动生成的主键 id
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    public Integer publishArticle(Article article);

    /**
     * 文章 分页 查询
     */
    @Select("select * from t_article order by id desc")
    public List<Article> selectArticleWithPage();

    /**
     * 通过 id 删除文章
     */
    @Delete("delete from t_article where id = #{id}")
    public void deleteArticleWithId(Integer id);

    /**
     * 统计文章数量
     */
    @Select("select count(1) from t_article")
    public Integer countArticle();

    /**
     * 通过 id 更新文章
     */
    public Integer updateArticleWithId(Article article);
}
