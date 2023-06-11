package stu.imzjm.model.domain;

/**
 * 用户实体类
 */
public class Account {
    Integer id;     //用户id
    String username;    //用户名
    String password;    //密文
    String email;   //注册邮箱
    String created;     //注册日期
    Integer valid;      //用户合法性, 默认 1
    Integer authorityId;    //用户权限  管理员 1,  一般 2

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public Integer getValid() {
        return valid;
    }

    public void setValid(Integer valid) {
        this.valid = valid;
    }

    public Integer getAuthorityId() {
        return authorityId;
    }

    public void setAuthorityId(Integer authorityId) {
        this.authorityId = authorityId;
    }
}
