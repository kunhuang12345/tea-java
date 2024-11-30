package com.bbs.teajava.service;

import com.bbs.teajava.entity.Friends;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bbs.teajava.entity.dto.UserResultDto;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author hk
 * @since 2024-11-25
 */
public interface IFriendsService extends IService<Friends> {

    /**
     * 添加好友
     *
     * @param friendId 好友id
     */
    void addFriend(Integer friendId) throws Exception;

    /**
     * 获取全部好友列表
     *
     * @return 好友列表
     */
    List<UserResultDto> getAllFriendList();
}
