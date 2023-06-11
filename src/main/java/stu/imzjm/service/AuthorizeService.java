package stu.imzjm.service;

import jakarta.annotation.Resource;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import stu.imzjm.dao.UserMapper;
import stu.imzjm.model.domain.Account;

@Service
public class AuthorizeService implements UserDetailsService {
    @Resource
    private UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //校验用户名
        if (username == null || username.equals("")) {
            throw new UsernameNotFoundException("用户名为空!");
        }

        //查询用户 及 用户权限代码
        Account account = userMapper.findAccountByUNameOrEmail(username);
        Integer authorityById = userMapper.findAuthorityById(account.getId());

        String pwd = account.getPassword();

        if (pwd == null) {
            throw new UsernameNotFoundException("用户名或密码错误!");
        }
        return User
                .withUsername(username)
                .password(pwd)
                .roles(authorityById == 1 ? "admin" : "user")
                .build();
    }
}
