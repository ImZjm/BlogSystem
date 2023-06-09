package stu.imzjm.web.client;

import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import stu.imzjm.model.domain.Article;
import stu.imzjm.model.domain.Comment;
import stu.imzjm.service.IArticleService;
import stu.imzjm.service.ICommentService;
import stu.imzjm.service.ISiteService;

import java.util.List;

@Controller
public class IndexController {
    private static final Logger logger = LoggerFactory.getLogger(IndexController.class);
    @Resource
    private IArticleService articleService;

    @Resource
    private ICommentService commentService;

    @Resource
    private ISiteService siteService;

    // 博客首页,自动跳转到文章页
    @GetMapping("/")
    private String index(HttpServletRequest request) {
        return this.index(request, 1, 5);
    }

    @GetMapping("/page/{p}")
    private String index(HttpServletRequest request,
                         @PathVariable("p") int page,
                         @RequestParam(value = "count", defaultValue = "5") int count) {
        PageInfo<Article> articles = articleService.selectArticleWithPage(page, count);

        //获取文章热度统计
        List<Article> articleList = articleService.getHeatArticles();
        request.setAttribute("articles", articles);
        request.setAttribute("articleList", articleList);

        logger.info("分页获取文章信息: 页码 " + page + ",条数 " + count);
        return "client/index";
    }

    //查看文章详情
    @GetMapping("/article/{id}")
    public String getArticleById(@PathVariable Integer id,
                                 HttpServletRequest request) {
        Article article = articleService.selectArticleWithId(id);
        if (article != null) {
            getArticleComments(request, article);

            //更新文章点击量
            siteService.updateStatistics(article);
            request.setAttribute("article", article);
            return "client/articleDetails";
        } else {
            logger.warn("查询文章详情结果为空, 查询文章 ID: " + id);

            return "error/404";
        }
    }

    //查询文章的评论信息,并补充到文章详情里
    private void getArticleComments(HttpServletRequest request, Article article) {
        //判断是否允许评论
        if (article.getAllowComment()) {
            // cp --- commentPage
            String cp = request.getParameter("cp");
            cp = StringUtils.isBlank(cp) ? "1" : cp;
            request.setAttribute("cp", cp);
            PageInfo<Comment> comments = commentService.getComments(article.getId(), Integer.parseInt(cp), 3);
            request.setAttribute("cp", cp);
            request.setAttribute("comments", comments);
        }
    }
}
