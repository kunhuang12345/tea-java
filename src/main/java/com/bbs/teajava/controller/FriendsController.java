package com.bbs.teajava.controller;


import com.bbs.teajava.annotation.Authentication;
import com.bbs.teajava.service.IFriendsService;
import com.bbs.teajava.utils.ApiResultUtils;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;


import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author hk
 * @since 2024-11-25
 */
@RestController
@RequestMapping("/friends")
@RequiredArgsConstructor
public class FriendsController {

    private final IFriendsService friendsService;

    @RequestMapping(value = "GetAllFriendList", method = {RequestMethod.GET, RequestMethod.POST})
    @ApiResponse(description = "获取全部好友列表")
    @Authentication
    public ApiResultUtils getAllFriendList() {
        return ApiResultUtils.success(friendsService.getAllFriendList());
    }

}
