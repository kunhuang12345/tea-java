package com.bbs.teajava.service.impl;

import com.bbs.teajava.config.ParamConfig;
import com.bbs.teajava.constants.RoleEnum;
import com.bbs.teajava.entity.Users;
import com.bbs.teajava.mapper.UsersMapper;
import com.bbs.teajava.service.IUsersService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bbs.teajava.utils.ApiResultUtils;
import com.bbs.teajava.utils.RandomUtil;
import com.bbs.teajava.utils.RedisUtil;
import com.bbs.teajava.utils.SessionUtils;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.*;
import java.util.List;
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
@RequiredArgsConstructor
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users> implements IUsersService {
    private final UsersMapper usersMapper;
    private final JavaMailSender mailSender;
    private final ParamConfig paramConfig;
    private final RedisUtil redis;
    // 密码加密
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    Logger logger = LoggerFactory.getLogger(UsersServiceImpl.class);

    @Override
    public List<Users> getAllUsers() {
        return usersMapper.getAllUsers();
    }

    @Override
    public ApiResultUtils sendEmailCode(String targetEmail, String checkCode, String subject, String content) {
        String imageCode = redis.get(targetEmail + "ImageCode");
        redis.delete(targetEmail + "ImageCode");
        if (!checkCode.equals(imageCode)) {
            return ApiResultUtils.error(400, "验证码错误");
        }
        List<String> codeInRedis = redis.range(targetEmail + "EmailCode", 0L, 1L);
        Random random = RandomUtil.getRandom();
        String code = String.format("%06d", random.nextInt(1000000));
        if (CollectionUtils.isNotEmpty(codeInRedis)) {
            int frequency = Integer.parseInt(codeInRedis.get(1));
            if (frequency > 2) {
                return ApiResultUtils.error(400, "邮件发送频率过高，请稍后再试");
            }
            redis.setListByIndex(targetEmail + "EmailCode", 0, code);
            redis.setListByIndex(targetEmail + "EmailCode", 1, String.valueOf(frequency + 1));
        } else {
            redis.rightPush(targetEmail + "EmailCode", 10, TimeUnit.MINUTES, code, "1");
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

    @Override
    public ApiResultUtils getImageCode(String email) {
        // 图片尺寸
        int width = 100;
        int height = 40;
        // 创建图片缓存
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        // 设置背景色
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);
        // 生成随机4位数字
        Random random = RandomUtil.getRandom();
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            code.append(random.nextInt(10));
        }
        // 将验证码存入redis
        redis.set(email + "ImageCode", code.toString(), 5, TimeUnit.MINUTES);
        // 绘制验证码
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 30));
        g.drawString(code.toString(), 20, 30);
        // 添加干扰线
        for (int i = 0; i < 6; i++) {
            g.setColor(new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256)));
            g.drawLine(random.nextInt(width), random.nextInt(height),
                    random.nextInt(width), random.nextInt(height));
        }
        // 释放图形上下文
        g.dispose();
        // 转换图片为字节数组
        try {
            ByteArrayOutputStream arrayStream = new ByteArrayOutputStream();
            ImageIO.write(image, "png", arrayStream);
            String base64Image = "data:image/png;base64," + Base64.getEncoder().encodeToString(arrayStream.toByteArray());
            return ApiResultUtils.success(base64Image);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return ApiResultUtils.error(500, "验证码生成失败");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResultUtils userRegister(String username, String email, String password, String emailCode) {
        List<String> codeInRedis = redis.range(email + "EmailCode", 0L, 1L);
        if (CollectionUtils.isEmpty(codeInRedis) ||!emailCode.equals(codeInRedis.get(0))) {
            return ApiResultUtils.error(400, "验证码错误");
        }
        Map<String, Object> columeMap = new HashMap<>();
        columeMap.put("email", email);
        if (CollectionUtils.isNotEmpty(usersMapper.selectByMap(columeMap))) {
            return ApiResultUtils.error(400, "邮箱已注册");
        }
        Users user = new Users();
        user.setUsername(username);
        user.setEmail(email);
        // 密码验证加密并填入数据库
        List<String> validated = this.validatePassword(password);
        if (CollectionUtils.isNotEmpty(validated)) {
            return ApiResultUtils.error(400, validated.toString());
        }
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(RoleEnum.USER.getValue());
        usersMapper.insert(user);
        redis.delete(email + "EmailCode");
        return ApiResultUtils.success("注册成功");
    }

    @Override
    public ApiResultUtils userLogin(String email, String password) {
        HttpSession session = SessionUtils.getSession();
        String sessionId = session.getId();
        Users userInSession = (Users) session.getAttribute(sessionId);
        if (userInSession != null) {
            return ApiResultUtils.error(400, "您已登录，请勿重复登录");
        }
        HashMap<String, Object> selectMap = new HashMap<>();
        selectMap.put("email", email);
        List<Users> users = usersMapper.selectByMap(selectMap);
        if (CollectionUtils.isEmpty(users)) {
            return ApiResultUtils.error(400, "用户未注册！");
        }
        Users user = users.get(0);
        if (!passwordEncoder.matches(password, user.getPassword())) {
            return ApiResultUtils.error(400, "密码错误");
        }
        session.setAttribute(sessionId, user);
        return ApiResultUtils.success("登录成功");
    }

    @Override
    public ApiResultUtils userLogout() {
        HttpSession session = SessionUtils.getSession();
        String sessionId = session.getId();
        Users userInSession = (Users) session.getAttribute(sessionId);
        if (userInSession != null) {
            session.removeAttribute(sessionId);
            return ApiResultUtils.success("退出成功");
        }
        return ApiResultUtils.error(400, "您尚未登录");
    }

    /**
     * 密码合格校验
     *
     * @param password 密码
     * @return 错误信息列表
     */
    private List<String> validatePassword(String password) {
        List<String> validationErrors = new ArrayList<>();

        if (password.length() < 8) {
            validationErrors.add("密码长度不能小于8位");
        }
        if (password.length() > 20) {
            validationErrors.add("密码长度不能小于20位");
        }
        if (!password.matches(".*[a-z].*")) {
            validationErrors.add("必须包含小写字母");
        }
        if (!password.matches(".*\\d.*")) {
            validationErrors.add("必须包含数字");
        }
        return validationErrors;
    }
}
