package stu.imzjm.config;

import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import stu.imzjm.service.AuthorizeService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Resource
    private AuthorizeService authorizeService;

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
                )

                .authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
                        .requestMatchers("/", "/page/**", "/article/**", "/login").permitAll()
                        .requestMatchers("/back/**", "/assets/**", "/user/**", "/article_img/**").permitAll()
                        .requestMatchers("/admin/**").hasRole("admin")
                        .anyRequest().authenticated()

                )

                .userDetailsService(authorizeService);


        return http.build();
    }

}
