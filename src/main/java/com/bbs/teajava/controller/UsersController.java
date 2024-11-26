package com.bbs.teajava.controller;


import com.bbs.teajava.service.IUsersService;
import com.bbs.teajava.utils.ApiResultUtils;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
@RequestMapping("/users")
public class UsersController {
    @Autowired
    private IUsersService usersService;

    @RequestMapping(value = "/getAllUsers", method = {RequestMethod.GET, RequestMethod.POST})
    @ApiResponse(description = "获取所有用户信息")
    public Object getAllUsers() {
        return ApiResultUtils.success(usersService.getAllUsers());
    }

}
