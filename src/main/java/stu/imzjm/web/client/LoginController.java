package stu.imzjm.web.client;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

//用户登录模块
@Controller
public class LoginController {
    //跳转登录页面
    @GetMapping("/login")
    public String login(HttpServletRequest request, Map<String, String> map) {
        String referer = request.getHeader("Referer");
        String url = request.getParameter("url");

        //如果url中封装了原始的页面, 则返回到原来的页面
        if (url != null && !url.equals("")) {
            map.put("url", url);
        }
        //本身包含登录
        else if (referer != null && referer.contains("/login")) {
            map.put("url", "");
        } else {
            map.put("url", referer);
        }

        return "comm/login";
    }

    @GetMapping("/errorPage/{page}/{code}")
    public String AccessExceptionHandler(@PathVariable String page,
                                         @PathVariable String code) {
        return page + "/" + code;
    }
}
