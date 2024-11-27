package com.bbs.teajava.service.impl;

import com.bbs.teajava.config.ParamConfig;
import com.bbs.teajava.entity.Users;
import com.bbs.teajava.mapper.UsersMapper;
import com.bbs.teajava.service.IUsersService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bbs.teajava.utils.ApiResultUtils;
import com.bbs.teajava.utils.RandomUtil;
import com.bbs.teajava.utils.RedisUtil;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 用户实现类
 * </p>
 *
 * @author hk
 * @since 2024-11-25
 */
@Service
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users> implements IUsersService {
    @Autowired
    private UsersMapper usersMapper;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private ParamConfig paramConfig;

    @Autowired
    private RedisUtil redisUtil;

    Logger logger = LoggerFactory.getLogger(UsersServiceImpl.class);

    @Override
    public List<Users> getAllUsers() {
        return usersMapper.getAllUsers();
    }

    @Override
    public ApiResultUtils sendEmail(String targetEmail, String subject, String content) {
        List<String> codeInRedis = redisUtil.range(targetEmail, 0L, 1L);
        Random random = RandomUtil.getRandom();
        String code = String.format("%06d", random.nextInt(1000000));
        if (CollectionUtils.isNotEmpty(codeInRedis)) {
            int frequency = Integer.parseInt(codeInRedis.get(1));
            if (frequency > 2) {
                return ApiResultUtils.error(400, "邮件发送频率过高，请稍后再试");
            }
            redisUtil.setListByIndex(targetEmail, 0, code);
            redisUtil.setListByIndex(targetEmail, 1, String.valueOf(frequency + 1));
        } else {
            redisUtil.rightPush(targetEmail, 10, TimeUnit.MINUTES, code, "1");
        }
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
            messageHelper.setFrom(new InternetAddress(paramConfig.getFromEmail(), paramConfig.getSendName(), "UTF-8"));
            messageHelper.setTo(targetEmail);
            messageHelper.setSubject(subject);
            messageHelper.setText(content + code, true);
            mailSender.send(mimeMessage);
            return ApiResultUtils.success("邮件发送成功");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return ApiResultUtils.error(500, "邮件发送失败");
        }
    }
}
