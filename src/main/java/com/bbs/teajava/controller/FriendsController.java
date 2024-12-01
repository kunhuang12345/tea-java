package com.bbs.teajava.controller;


import com.bbs.teajava.annotation.Authentication;
import com.bbs.teajava.service.IFriendsService;
import com.bbs.teajava.utils.ApiResultUtils;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;


import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 关系接口
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

    @RequestMapping(value = "AddFollow", method = {RequestMethod.POST})
    @ApiOperation("添加关注")
    @Authentication
    public ApiResultUtils addFollow(@RequestParam(value = "userId") Integer userId) {
        return friendsService.addFollow(userId);
    }

    @RequestMapping(value = "CancelFollow", method = {RequestMethod.POST})
    @ApiOperation("取消关注")
    @Authentication
    public ApiResultUtils cancelFollow(@RequestParam(value = "userId") Integer userId) {
        return friendsService.cancelFollow(userId);
    }

    @RequestMapping(value = "GetAllFollowList", method = {RequestMethod.GET, RequestMethod.POST})
    @ApiOperation("获取全部关注列表")
    @Authentication
    public Object getAllFollowList() {
        return ApiResultUtils.success(friendsService.getAllFollowList());
    }

    @RequestMapping(value = "GetAllFriendList", method = {RequestMethod.GET, RequestMethod.POST})
    @ApiOperation("获取全部好友列表")
    @Authentication
    public ApiResultUtils getAllFriendList() {
        return ApiResultUtils.success(friendsService.getAllFriendList());
    }

    @RequestMapping(value = "DeleteFriend", method = {RequestMethod.POST})
    @ApiOperation("删除好友")
    @Authentication
    public ApiResultUtils deleteFriend(@RequestParam(value = "friendId") Integer friendId) {
        return friendsService.deleteFriend(friendId);
    }

}
