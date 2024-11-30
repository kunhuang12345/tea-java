package com.bbs.teajava.service.impl;

import com.bbs.teajava.entity.Friends;
import com.bbs.teajava.entity.Users;
import com.bbs.teajava.mapper.FriendsMapper;
import com.bbs.teajava.mapper.UsersMapper;
import com.bbs.teajava.service.IFriendsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bbs.teajava.utils.ApiResultUtils;
import com.bbs.teajava.utils.SessionUtils;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
    public ApiResultUtils addFriend(String friendId) {
        Users friendInfo = usersMapper.selectById(friendId);
        if (friendInfo == null) {
            return ApiResultUtils.error(500, "用户不存在");
        }
        Friends friend = new Friends();
        // TODO: 申请
        return null;
    }
}
