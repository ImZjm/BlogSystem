package stu.imzjm.config;

import jakarta.annotation.Resource;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import stu.imzjm.service.AuthorizeService;

import java.io.IOException;
import java.net.URL;
import java.util.Collection;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Resource
    private AuthorizeService authorizeService;

    @Value("${COOKIE.VALIDITY}")
    private Integer COOKIE_VALIDITY;


    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http

                .formLogin(formLogin -> formLogin
                        .loginPage("/login")
                        .usernameParameter("username")
                        .passwordParameter("password")
                        .successHandler(this::onAuthenticationSuccess)
                        .failureHandler(this::onAuthenticationFailure)
                )

                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .accessDeniedHandler(this::accessDeniedHandler)
                )

                .authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
                        .requestMatchers("/", "/page/**", "/article/**", "/login").permitAll()
                        .requestMatchers("/back/**", "/assets/**", "/user/**", "/article_img/**").permitAll()
                        .requestMatchers("/admin/**").hasRole("admin")
                        .anyRequest().authenticated()
                )

                .userDetailsService(authorizeService)

                .rememberMe(rememberMe -> rememberMe
                        .alwaysRemember(true)
                        .tokenValiditySeconds(COOKIE_VALIDITY)
                );


        return http.build();
    }

    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String url = request.getParameter("url");

        RequestCache requestCache = new HttpSessionRequestCache();
        SavedRequest savedRequest = requestCache.getRequest(request, response);

        if (savedRequest != null)
            response.sendRedirect(savedRequest.getRedirectUrl());
        else if (url != null && !url.equals(""))
            response.sendRedirect(new URL(url).getPath());
        else {
            //直接访问登录页面登录的角色,根据用户角色 分别重定向到首页或后台
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            boolean isAdmin = authorities.contains(new SimpleGrantedAuthority("ROLE_admin"));
            if (isAdmin)
                response.sendRedirect("/admin");
            else
                response.sendRedirect("/");
        }
    }

    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        String url = request.getParameter("url");
        response.sendRedirect("/login?error&url=" + url);
    }

    public void accessDeniedHandler(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("/error/403");
        dispatcher.forward(request, response);
    }

}
