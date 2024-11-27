package com.bbs.teajava.controller;


import com.bbs.teajava.service.IUsersService;
import com.bbs.teajava.utils.ApiResultUtils;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
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
public class UsersController {
    @Autowired
    private IUsersService usersService;

    @RequestMapping(value = "/GetAllUsers", method = {RequestMethod.GET, RequestMethod.POST})
    @ApiResponse(description = "获取所有用户信息")
    public Object getAllUsers() {
        return ApiResultUtils.success(usersService.getAllUsers());
    }

    @RequestMapping(value = "SendEmail", method = {RequestMethod.POST})
    @ApiResponse(description = "发送邮件验证码")
    public ApiResultUtils sendEmail(@RequestParam(value = "targetEmail") String targetEmail,
                                    @RequestParam(value = "subject") String  subject,
                                    @RequestParam(value = "content") String content) {
        return usersService.sendEmail(targetEmail, subject, content);
    }

}
