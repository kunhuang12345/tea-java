package com.bbs.teajava.controller;


import com.bbs.teajava.annotation.Authentication;
import com.bbs.teajava.service.IFriendRequestsService;
import com.bbs.teajava.utils.ApiResultUtils;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 好友申请接口
 * </p>
 *
 * @author hk
 * @since 2024-11-30
 */
@RestController
@RequestMapping("/friendRequests")
@RequiredArgsConstructor
public class FriendRequestsController {

    private final IFriendRequestsService friendRequestsService;
    @RequestMapping(value = "ApplyFriend", method = {RequestMethod.POST})
    @ApiResponse(description = "申请好友")
    @Authentication
    public ApiResultUtils applyFriend(@RequestParam(value = "friendId") Integer friendId,
                                      @RequestParam(value = "message") String message) {
        return friendRequestsService.applyFriend(friendId, message);
    }

}
