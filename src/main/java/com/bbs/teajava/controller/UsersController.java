package com.bbs.teajava.controller;


import com.bbs.teajava.annotation.Authentication;
import com.bbs.teajava.service.IUsersService;
import com.bbs.teajava.utils.ApiResultUtils;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
 * <p>
 * 用户类
 * </p>
 *
 * @author hk
 * @since 2024-11-25
 */
@RestController
@RequestMapping("/Users")
@RequiredArgsConstructor
public class UsersController {
    private final IUsersService usersService;

    @RequestMapping(value = "/GetAllUsers", method = {RequestMethod.GET, RequestMethod.POST})
    @ApiOperation("获取所有用户信息")
    public ApiResultUtils getAllUsers() {
        return ApiResultUtils.success(usersService.getAllUsers());
    }

    @RequestMapping(value = "GetInfo", method = {RequestMethod.GET, RequestMethod.POST})
    @ApiOperation("获取用户信息")
    @Authentication
    public ApiResultUtils getInfo() {
        return ApiResultUtils.success(usersService.getInfo());
    }

    @RequestMapping(value = "GetInfoById", method = {RequestMethod.GET, RequestMethod.POST})
    @ApiOperation("获取用户信息")
    @Authentication
    public ApiResultUtils getInfoById(@RequestParam("id") Integer id) {
        return usersService.getInfoById(id);
    }

    @RequestMapping(value = "GetInfoByEmail", method = {RequestMethod.GET, RequestMethod.POST})
    @ApiOperation("获取用户信息")
    @Authentication
    public ApiResultUtils getInfoById(@RequestParam("email") String email) {
        return usersService.getInfoByEmail(email);
    }



    @RequestMapping(value = "SendEmailCode", method = {RequestMethod.POST})
    @ApiOperation("发送邮件验证码")
    public ApiResultUtils sendEmailCode(@RequestParam(value = "targetEmail") String targetEmail,
                                        @RequestParam(value = "imageCode") String imageCode,
                                        @RequestParam(value = "subject") String subject,
                                        @RequestParam(value = "content") String content) {
        return usersService.sendEmailCode(targetEmail, imageCode, subject, content);
    }



    @RequestMapping(value = "GetImageCode", method = {RequestMethod.GET, RequestMethod.POST})
    @ApiOperation("获取图片验证码")
    public ApiResultUtils getImageCode(@RequestParam(value = "email") String email) {
        return usersService.getImageCode(email);
    }

    @RequestMapping(value = "UserRegister", method = {RequestMethod.POST})
    @ApiOperation("注册用户")
    public ApiResultUtils userRegister(@RequestParam(value = "username") String username,
                                       @RequestParam(value = "email") String email,
                                       @RequestParam(value = "password") String password,
                                       @RequestParam(value = "emailCode") String emailCode) {
        return usersService.userRegister(username, email, password, emailCode);
    }


    @RequestMapping(value = "UserLogin", method = {RequestMethod.POST})
    @ApiOperation("用户登录")
    public ApiResultUtils userLogin(@RequestParam(value = "email") String email,
                                    @RequestParam(value = "password") String password) {
        return usersService.userLogin(email, password);
    }

    @RequestMapping(value = "UserLogout", method = {RequestMethod.POST})
    @ApiOperation("用户注销")
    @Authentication
    public ApiResultUtils userLogout() {
        return usersService.userLogout();
    }

    @RequestMapping(value = "UserAlterPassword", method = {RequestMethod.POST})
    @ApiOperation("修改密码(每7天限制修改三次)")
    public ApiResultUtils userAlterPassword(@RequestParam(value = "email") String email,
                                            @RequestParam(value = "oldPassword") String oldPassword,
                                            @RequestParam(value = "newPassword") String newPassword) {
        return usersService.userAlterPassword(email, oldPassword, newPassword);
    }

    @RequestMapping(value = "Mute", method = {RequestMethod.POST})
    @ApiOperation("用户禁言 传入时间格式: yyyy-MM-dd HH:mm")
    @Authentication(requireAdmin = true)
    public ApiResultUtils mute(@RequestParam(value = "userId") Integer userId,
                               @RequestParam(value = "datetime") String datetime) {
        return usersService.mute(userId, datetime);
    }

}
