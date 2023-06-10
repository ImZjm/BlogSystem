package stu.imzjm.web.admin;

import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import stu.imzjm.model.ResponseData.ArticleResponseData;
import stu.imzjm.model.ResponseData.StaticticsBo;
import stu.imzjm.model.domain.Article;
import stu.imzjm.model.domain.Comment;
import stu.imzjm.service.IArticleService;
import stu.imzjm.service.ISiteService;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    @Resource
    private ISiteService iSiteService;

    @Resource
    private IArticleService iArticleService;

    //后台首页
    @GetMapping({"", "/index"})
    public String index(HttpServletRequest request) {
        //获取最新的 5 篇博客、评论以及统计数据
        List<Article> articles = iSiteService.recentArticles(5);
        List<Comment> comments = iSiteService.recentComments(5);
        StaticticsBo statistics = iSiteService.getStatistics();

        //向Request中存储数据
        request.setAttribute("articles", articles);
        request.setAttribute("comments", comments);
        request.setAttribute("statistics", statistics);
        return "back/index";
    }

    //文章 编辑,发表 页面
    @GetMapping("/article/toEditPage")
    String newArticle() {
        return "back/article_edit";
    }

    //发表文章
    @PostMapping("/article/publish")
    @ResponseBody
    ArticleResponseData publishArticle(Article article) {
        // 检查 文章分类 是否为空白
        if (StringUtils.isBlank(article.getCategories())) {
            article.setCategories("默认分类");
        }
        try {
            iArticleService.publish(article);
            logger.info("文章发布成功");
            return ArticleResponseData.ok();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("文章发布失败, 错误信息: " + e.getMessage());
            return ArticleResponseData.fail();
        }
    }

    //跳到后台文章列表页面
    @GetMapping("/article")
    String index(@RequestParam(value = "page", defaultValue = "1") int page,
                 @RequestParam(defaultValue = "10") int count,
                 HttpServletRequest request) {
        PageInfo<Article> pageInfo = iArticleService.selectArticleWithPage(page, count);
        request.setAttribute("articles", pageInfo);

        return "back/article_list";
    }

    //跳转道文章修改页面
    @GetMapping("/article/{id}")
    public String editArticle(@PathVariable("id") String id,
                              HttpServletRequest request) {
        Article article = iArticleService.selectArticleWithId(Integer.parseInt(id));
        request.setAttribute("contents", article);
        request.setAttribute("categories", article.getCategories());
        return "back/article_edit";
    }

    //提交文章修改
    @PostMapping("/article/modify")
    @ResponseBody
    public ArticleResponseData modifyArticle(Article article) {
        try {
            iArticleService.updateArticleWithId(article);
            logger.info("文章更新成功!");
            return ArticleResponseData.ok();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("文章更新失败, 错误信息: " + e.getMessage());
            return ArticleResponseData.fail();
        }
    }

    //文章删除
    @PostMapping("/article/delete")
    @ResponseBody
    public ArticleResponseData delete(@RequestParam int id) {
        try {
            iArticleService.deleteArticleWithId(id);
            logger.info("文章删除成功!");
            return ArticleResponseData.ok();
        } catch (Exception e) {
            logger.error("文章删除失败,错误信息: " + e.getMessage());
            return ArticleResponseData.fail();
        }
    }
}
