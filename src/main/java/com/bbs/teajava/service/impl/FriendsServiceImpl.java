package com.bbs.teajava.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bbs.teajava.constants.RelationshipEnum;
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
 * 好友实现类
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
        // 添加自己与对方记录
        QueryWrapper<Friends> queryWrapper = new QueryWrapper<Friends>().eq("user_id", user.getId()).eq("friend_id", friendId);
        Friends userShip = friendsMapper.selectOne(queryWrapper);
        // 记录存在标志
        int tag = 1;
        if (userShip == null) {
            tag = 0;
            userShip = new Friends();
            userShip.setIsFollow(RelationshipEnum.NOT_FOLLOW.getValue());
        }
        userShip.setUserId(user.getId());
        userShip.setFriendId(friendId);
        userShip.setIsFriend(RelationshipEnum.FRIENDS.getValue());
        if (tag == 0) {
            friendsMapper.insert(userShip);
        } else {
            friendsMapper.update(userShip, queryWrapper);
        }
        // 对方关系记录添加
        queryWrapper.clear();
        queryWrapper = new QueryWrapper<Friends>().eq("user_id", friendId).eq("friend_id", user.getId());
        userShip = friendsMapper.selectOne(queryWrapper);
        if (userShip == null) {
            userShip = new Friends();
            userShip.setIsFollow(RelationshipEnum.NOT_FOLLOW.getValue());
        }
        userShip.setUserId(friendId);
        userShip.setFriendId(user.getId());
        userShip.setIsFriend(RelationshipEnum.FRIENDS.getValue());
        if (tag == 0) {
            friendsMapper.insert(userShip);
        } else {
            friendsMapper.update(userShip, queryWrapper);
        }
    }

    @Override
    public List<UserResultDto> getAllFriendList() {
        Users user = SessionUtils.getUser();
        QueryWrapper<Friends> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", user.getId());
        queryWrapper.eq("is_friend", RelationshipEnum.FRIENDS.getValue());
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
        // 删除好友关系
        QueryWrapper<Friends> queryWrapper = new QueryWrapper<Friends>().eq("user_id", user.getId()).eq("friend_id", friendId);
        Friends userShip = friendsMapper.selectOne(queryWrapper);
        userShip.setIsFriend(RelationshipEnum.NOT_FRIENDS.getValue());
        int update0 = friendsMapper.update(userShip, queryWrapper);
        // 删除对方的好友关系
        queryWrapper.clear();
        queryWrapper.eq("user_id", friendId).eq("friend_id", user.getId());
        userShip = friendsMapper.selectOne(queryWrapper);
        userShip.setIsFriend(RelationshipEnum.NOT_FRIENDS.getValue());
        int update1 = friendsMapper.update(userShip, queryWrapper);
        return ApiResultUtils.success(update0 + update1);
    }
}
