package com.bbs.teajava.service.impl;

import com.bbs.teajava.constants.FriendRequestStatusEnum;
import com.bbs.teajava.entity.FriendRequests;
import com.bbs.teajava.entity.Users;
import com.bbs.teajava.mapper.FriendRequestsMapper;
import com.bbs.teajava.mapper.UsersMapper;
import com.bbs.teajava.service.IFriendRequestsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bbs.teajava.utils.ApiResultUtils;
import com.bbs.teajava.utils.SessionUtils;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hk
 * @since 2024-11-30
 */
@Service
@RequiredArgsConstructor
public class FriendRequestsServiceImpl extends ServiceImpl<FriendRequestsMapper, FriendRequests> implements IFriendRequestsService {
    private final UsersMapper usersMapper;
    private final FriendRequestsMapper friendRequestsMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResultUtils applyFriend(Integer friendId, String message) {
        Users friendInfo = usersMapper.selectById(friendId);
        if (friendInfo == null) {
            return ApiResultUtils.error(500, "该用户不存在");
        }
        HttpSession session = SessionUtils.getSession();
        Users user = (Users) session.getAttribute(session.getId());
        FriendRequests friendRequests = new FriendRequests();
        friendRequests.setFromUserId(user.getId());
        friendRequests.setToUserId(friendId);
        friendRequests.setMessage(message);
        friendRequests.setStatus(FriendRequestStatusEnum.PENDING.getValue());
        friendRequests.setCreateTime(LocalDateTime.now());
        friendRequestsMapper.insert(friendRequests);
        return ApiResultUtils.success();
    }
}
