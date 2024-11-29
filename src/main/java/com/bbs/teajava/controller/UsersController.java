package com.bbs.teajava.controller;


import com.bbs.teajava.service.IUsersService;
import com.bbs.teajava.utils.ApiResultUtils;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
    @ApiResponse(description = "获取所有用户信息")
    public ApiResultUtils getAllUsers() {
        return ApiResultUtils.success(usersService.getAllUsers());
    }

    @RequestMapping(value = "SendEmailCode", method = {RequestMethod.POST})
    @ApiResponse(description = "发送邮件验证码")
    public ApiResultUtils sendEmailCode(@RequestParam(value = "targetEmail") String targetEmail,
                                        @RequestParam(value = "imageCode") String imageCode,
                                        @RequestParam(value = "subject") String subject,
                                        @RequestParam(value = "content") String content) {
        return usersService.sendEmailCode(targetEmail, imageCode, subject, content);
    }

    @RequestMapping(value = "GetImageCode", method = {RequestMethod.GET, RequestMethod.POST})
    @ApiResponse(description = "获取图片验证码")
    public ApiResultUtils getImageCode(@RequestParam(value = "email") String email) {
        return usersService.getImageCode(email);
    }

    @RequestMapping(value = "UserRegister", method = {RequestMethod.POST})
    @ApiResponse(description = "注册用户")
    public ApiResultUtils userRegister(@RequestParam(value = "username") String username,
                                       @RequestParam(value = "email") String email,
                                       @RequestParam(value = "password") String password,
                                       @RequestParam(value = "emailCode") String emailCode) {
        return usersService.userRegister(username, email, password, emailCode);
    }

    @RequestMapping(value = "UserLogin", method = {RequestMethod.GET, RequestMethod.POST})
    @ApiResponse(description = "用户登录")
    public ApiResultUtils userLogin(@RequestParam(value = "email") String email,
                                    @RequestParam(value = "password") String password) {
        return usersService.userLogin(email, password);
    }
    
    @RequestMapping(value = "UserLogout", method = {RequestMethod.POST})
    @ApiResponse(description = "用户注销")
    public ApiResultUtils userLogout() {
        return usersService.userLogout();
    }

}
