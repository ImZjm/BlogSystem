package stu.imzjm.web.client;

import com.vdurmont.emoji.EmojiParser;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import stu.imzjm.model.ResponseData.ArticleResponseData;
import stu.imzjm.model.domain.Comment;
import stu.imzjm.service.ICommentService;
import stu.imzjm.utils.MyUtils;

import java.util.Date;

@Controller
@RequestMapping("/comments")
public class CommentController {
    private static final Logger logger = LoggerFactory.getLogger(CommentController.class);

    @Resource
    private ICommentService commentService;

    @PostMapping("/publish")
    @ResponseBody
    public ArticleResponseData publishComment(HttpServletRequest request,
                                              @RequestParam Integer aid, @RequestParam String text) {
        text = MyUtils.cleanXSS(text);
        text = EmojiParser.parseToAliases(text);

        //获取当前登录用户
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        //封装评论信息
        Comment comment = new Comment();
        comment.setArticleId(aid);
        comment.setIp(request.getRemoteAddr());
        comment.setCreated(new Date());
        comment.setAuthor(user.getUsername());
        comment.setContent(text);

        try {
            commentService.pushComment(comment);
            logger.info("发布评论成功, 对应文章ID: " + aid);
            return ArticleResponseData.ok();
        } catch (Exception e) {
            logger.error("发布评论失败,对应文章ID: " + aid + "; 错误描述: " + e.getMessage());
            return ArticleResponseData.fail();
        }
    }
}
