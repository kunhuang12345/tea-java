package com.bbs.teajava.controller;


import com.bbs.teajava.annotation.Authentication;
import com.bbs.teajava.service.IFriendRequestsService;
import com.bbs.teajava.utils.ApiResultUtils;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping(value = "ApplyFriend")
    @ApiOperation("添加好友")
    @Authentication
    public ApiResultUtils applyFriend(@RequestParam(value = "friendId") Integer friendId,
                                      @RequestParam(value = "message", required = false) String message) {
        return friendRequestsService.applyFriend(friendId, message);
    }

    @PostMapping(value = "ApplyFriendByEmail")
    @ApiOperation("通过邮箱添加好友")
    @Authentication
    public ApiResultUtils applyFriendByEmail(@RequestParam(value = "email") Integer email,
                                      @RequestParam(value = "message", required = false) String message) {
        return friendRequestsService.applyFriendByEmail(email, message);
    }




    @RequestMapping(value = "GetApplyFriendList", method = {RequestMethod.GET, RequestMethod.POST})
    @ApiOperation("获取好友申请列表")
    @Authentication
    public ApiResultUtils getApplyFriendList() {
        return ApiResultUtils.success(friendRequestsService.getApplyFriendList());
    }

    @RequestMapping(value = "GetReceivedApplyList", method = {RequestMethod.GET, RequestMethod.POST})
    @ApiOperation("获取收到的好友申请列表")
    @Authentication
    public ApiResultUtils getReceivedApplyList() {
        return ApiResultUtils.success(friendRequestsService.getReceivedApplyList());
    }

    @PostMapping(value = "HandleFriendApply")
    @ApiOperation("处理好友申请 status: 1-同意 2-拒绝")
    @Authentication
    public ApiResultUtils handleFriendApply(@RequestParam(value = "applyId") Integer applyId,
                                            @RequestParam(value = "status") Integer status) {
        return friendRequestsService.handleFriendApply(applyId, status);
    }

}
