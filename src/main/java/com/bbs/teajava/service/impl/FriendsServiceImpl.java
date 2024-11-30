package com.bbs.teajava.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bbs.teajava.entity.Friends;
import com.bbs.teajava.entity.Users;
import com.bbs.teajava.entity.dto.UserResultDto;
import com.bbs.teajava.mapper.FriendsMapper;
import com.bbs.teajava.mapper.UsersMapper;
import com.bbs.teajava.service.IFriendsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bbs.teajava.utils.ApiResultUtils;
import com.bbs.teajava.utils.SessionUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
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

    @Override
    public List<UserResultDto> getAllFriendList() {
        Users user = SessionUtils.getUser();
        QueryWrapper<Friends> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", user.getId());
        List<Friends> friendList = friendsMapper.selectList(queryWrapper);
        List<Integer> idList = friendList.stream().map(Friends::getFriendId).toList();
        if (CollectionUtils.isEmpty(idList)) {
            return Collections.emptyList();
        }
        List<UserResultDto> dtoList = new ArrayList<>();
        List<Users> userList = usersMapper.selectBatchIds(idList);
        for (Users friend : userList) {
            UserResultDto dto = new UserResultDto();
            BeanUtils.copyProperties(friend, dto);
            dtoList.add(dto);
        }
        return dtoList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResultUtils deleteFriend(Integer friendId) {
        Users user = SessionUtils.getUser();
        int count = friendsMapper.delete(user.getId(), friendId);
        int count1 = friendsMapper.delete(friendId, user.getId());
        return ApiResultUtils.success(count + count1);
    }
}
