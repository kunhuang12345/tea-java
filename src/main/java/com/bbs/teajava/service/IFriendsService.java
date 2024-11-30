package com.bbs.teajava.service;

import com.bbs.teajava.entity.Friends;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bbs.teajava.utils.ApiResultUtils;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author hk
 * @since 2024-11-25
 */
public interface IFriendsService extends IService<Friends> {

    /**
     * 添加好友
     * @param friendId 好友id
     * @return 添加结果
     */
    ApiResultUtils addFriend(String friendId);
}
