package com.bbs.teajava.controller;


import com.bbs.teajava.annotation.Authentication;
import com.bbs.teajava.service.IFriendsService;
import com.bbs.teajava.utils.ApiResultUtils;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
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

    @RequestMapping(value = "addFriend", method = {RequestMethod.POST})
    @ApiResponse(description = "添加好友")
    @Authentication
    public ApiResultUtils addFriend(@RequestParam(value = "friendId") String friendId) {
        return friendsService.addFriend(friendId);
    }

}
