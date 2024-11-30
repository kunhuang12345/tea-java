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
    @ApiResponse(description = "添加好友")
    @Authentication
    public ApiResultUtils applyFriend(@RequestParam(value = "friendId") Integer friendId,
                                      @RequestParam(value = "message", required = false) String message) {
        return friendRequestsService.applyFriend(friendId, message);
    }

    @RequestMapping(value = "GetApplyFriendList", method = {RequestMethod.GET, RequestMethod.POST})
    @ApiResponse(description = "获取好友申请列表")
    @Authentication
    public ApiResultUtils getApplyFriendList() {
        return ApiResultUtils.success(friendRequestsService.getApplyFriendList());
    }

    @RequestMapping(value = "GetReceivedApplyList", method = {RequestMethod.GET, RequestMethod.POST})
    @ApiResponse(description = "获取收到的好友申请列表")
    @Authentication
    public ApiResultUtils getReceivedApplyList() {
        return ApiResultUtils.success(friendRequestsService.getReceivedApplyList());
    }

    @RequestMapping(value = "HandleFriendApply", method = {RequestMethod.POST})
    @ApiResponse(description = "处理好友申请 status: 1-同意 2-拒绝")
    @Authentication
    public ApiResultUtils handleFriendApply(@RequestParam(value = "applyId") Integer applyId,
                                            @RequestParam(value = "status") Integer status) {
        return friendRequestsService.handleFriendApply(applyId, status);
    }

}
