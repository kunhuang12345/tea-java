package com.bbs.teajava.service;

import com.bbs.teajava.entity.Users;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bbs.teajava.entity.dto.UserResultDto;
import com.bbs.teajava.utils.ApiResultUtils;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author hk
 * @since 2024-11-25
 */
public interface IUsersService extends IService<Users> {

    List<Users> getAllUsers();

    /**
     * 发送邮件验证码
     *
     * @param targetEmail 来源邮箱信息
     * @param checkCode   图片验证码检验
     * @param subject     邮件标题
     * @param content     邮件内容
     * @return 发送状态
     */
    ApiResultUtils sendEmailCode(String targetEmail, String checkCode, String subject, String content);

    /**
     * 获取图片验证码
     *
     * @return 图片验证码
     */
    ApiResultUtils getImageCode(String email);

    /**
     * 注册用户
     *
     * @param username  用户名
     * @param email     邮箱
     * @param password  密码
     * @param emailCode 邮箱验证码
     * @return 注册结果
     */
    ApiResultUtils userRegister(String username, String email, String password, String emailCode);

    /**
     * 用户登录
     *
     * @param email    登录邮箱
     * @param password 登录密码
     * @return 登录结果
     */
    ApiResultUtils userLogin(String email, String password);

    /**
     * 用户注销
     *
     * @return 注销结果
     */
    ApiResultUtils userLogout();

    /**
     * 修改用户密码(限制每7天3次)
     *
     * @param email       邮箱
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @return 修改结果
     */
    ApiResultUtils userAlterPassword(String email, String oldPassword, String newPassword);

    /**
     * 会议报告人注册
     *
     * @param id 注册用户id
     */
    void reporterRegister(Integer id);

    /**
     * 获取用户信息
     *
     * @return 用户信息
     */
    UserResultDto getInfo();
}
