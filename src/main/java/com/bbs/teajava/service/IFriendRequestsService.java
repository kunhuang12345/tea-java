package com.bbs.teajava.service;

import com.bbs.teajava.entity.FriendRequests;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bbs.teajava.entity.dto.FriendRequestsResultDto;
import com.bbs.teajava.utils.ApiResultUtils;

import java.util.List;

/**
 * <p>
 *  业务类
 * </p>
 *
 * @author hk
 * @since 2024-11-30
 */
public interface IFriendRequestsService extends IService<FriendRequests> {

    /**
     * 申请好友
     *
     * @param friendId 好友ID
     * @param message 申请信息
     * @return 接口结果
     */
    ApiResultUtils applyFriend(Integer friendId, String message);

    /**
     * 获取好友申请列表
     *
     * @return 好友申请列表
     */
    List<FriendRequestsResultDto> getApplyFriendList();

    /**
     * 获取收到的好友申请列表
     *
     * @return 收到的好友申请列表
     */
    List<FriendRequestsResultDto> getReceivedApplyList();

    /**
     * 处理好友申请
     *  1. 同意：添加好友
     *  2. 拒绝好友申请
     *
     * @param applyId 申请ID
     * @param status  处理状态
     * @return 接口结果
     */
    ApiResultUtils handleFriendApply(Integer applyId, Integer status);


    /**
     * 通过邮箱申请好友
     *
     * @param email 好友邮箱
     * @param message 申请信息
     * @return 接口结果
     */
    ApiResultUtils applyFriendByEmail(Integer email, String message);
}
