package com.bbs.teajava.service;

import com.bbs.teajava.entity.Users;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bbs.teajava.utils.ApiResultUtils;

import java.util.List;

/**
 * <p>
 *  服务类
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
     * @param subject 邮件标题
     * @param content 邮件内容
     * @return 发送状态
     */
    ApiResultUtils sendEmail(String targetEmail, String subject, String content);
}
