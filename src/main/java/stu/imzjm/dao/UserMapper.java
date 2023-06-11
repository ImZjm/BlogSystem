package stu.imzjm.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import stu.imzjm.model.domain.Account;

/**
 * 用户表操作
 */
@Mapper
public interface UserMapper {
    /**
     * 通过用户名或邮箱匹配密码
     *
     * @param text 用户名 或 邮箱
     * @return 被加密过的 密码
     */
    @Select("select * from t_user where username = #{text} or email = #{text}")
    Account findAccountByUNameOrEmail(String text);

    /**
     * 根据 用户id 查找用户权限
     *
     * @param userId 用户id
     * @return 用户权限 (int)
     * <pre>1 - 管理员(admin)</pre>
     * <pre>2 - 一般成员</pre>
     */
    @Select("select authority_id from t_user_authority where user_id = #{userId}")
    Integer findAuthorityById(Integer userId);
}
