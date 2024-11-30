package com.bbs.teajava.service.impl;

import com.bbs.teajava.entity.Friends;
import com.bbs.teajava.entity.Users;
import com.bbs.teajava.mapper.FriendsMapper;
import com.bbs.teajava.mapper.UsersMapper;
import com.bbs.teajava.service.IFriendsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bbs.teajava.utils.SessionUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  好友实现类
 * </p>
 *
 * @author hk
 * @since 2024-11-25
 */
@Service
@RequiredArgsConstructor
public class FriendsServiceImpl extends ServiceImpl<FriendsMapper, Friends> implements IFriendsService {

    private final FriendsMapper friendsMapper;
    private final UsersMapper usersMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addFriend(Integer friendId) throws Exception {
        Users friendInfo = usersMapper.selectById(friendId);
        if (friendInfo == null) {
            throw new Exception("该用户不存在");
        }
        Users user = SessionUtils.getUser();
        List<Friends> friendList = new ArrayList<>();
        Friends friend = new Friends();
        friend.setUserId(user.getId());
        friend.setFriendId(friendId);
        friendList.add(friend);
        friend = new Friends();
        friend.setUserId(friendId);
        friend.setFriendId(user.getId());
        friendList.add(friend);
        friendsMapper.batchInsert(friendList);
    }
}
