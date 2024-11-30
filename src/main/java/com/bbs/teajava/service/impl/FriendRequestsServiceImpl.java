package com.bbs.teajava.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bbs.teajava.constants.FriendRequestStatusEnum;
import com.bbs.teajava.entity.FriendRequests;
import com.bbs.teajava.entity.Users;
import com.bbs.teajava.entity.dto.FriendRequestsResultDto;
import com.bbs.teajava.mapper.FriendRequestsMapper;
import com.bbs.teajava.mapper.UsersMapper;
import com.bbs.teajava.service.IFriendRequestsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bbs.teajava.service.IFriendsService;
import com.bbs.teajava.utils.ApiResultUtils;
import com.bbs.teajava.utils.SessionUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 服务实现类
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
    private final IFriendsService friendService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResultUtils applyFriend(Integer friendId, String message) {
        Users friendInfo = usersMapper.selectById(friendId);
        if (friendInfo == null) {
            return ApiResultUtils.error(500, "该用户不存在");
        }
        Users user = SessionUtils.getUser();
        QueryWrapper<FriendRequests> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("from_user_id", user.getId()).eq("to_user_id", friendId);
        FriendRequests friendRequestResult = friendRequestsMapper.selectOne(queryWrapper);
        FriendRequests friendRequests = new FriendRequests();
        if (friendRequestResult != null) {
            friendRequests.setId(friendRequestResult.getId());
        }
        friendRequests.setFromUserId(user.getId());
        friendRequests.setToUserId(friendId);
        friendRequests.setMessage(message);
        friendRequests.setStatus(FriendRequestStatusEnum.PENDING.getValue());
        friendRequests.setCreateTime(LocalDateTime.now());
        if (friendRequestResult == null) {
            friendRequestsMapper.insert(friendRequests);
        } else {
            friendRequestsMapper.updateById(friendRequests);
        }
        return ApiResultUtils.success();
    }

    @Override
    public List<FriendRequestsResultDto> getApplyFriendList() {
        Users user = SessionUtils.getUser();
        QueryWrapper<FriendRequests> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("from_user_id", user.getId());
        return this.getFriendApplyList(queryWrapper);
    }

    @Override
    public List<FriendRequestsResultDto> getReceivedApplyList() {
        Users user = SessionUtils.getUser();
        QueryWrapper<FriendRequests> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("to_user_id", user.getId());
        return this.getFriendApplyList(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResultUtils handleFriendApply(Integer applyId, Integer status) {
        if (status != 1 && status != 2) {
            return ApiResultUtils.error(500, "参数错误");
        }
        Users user = SessionUtils.getUser();
        QueryWrapper<FriendRequests> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("from_user_id", applyId).eq("to_user_id", user.getId());
        FriendRequests friendRequestResult = friendRequestsMapper.selectOne(queryWrapper);
        if (friendRequestResult == null) {
            return ApiResultUtils.error(500, "该申请不存在");
        }
        friendRequestResult.setStatus(status);
        friendRequestsMapper.updateById(friendRequestResult);
        try {
            friendService.addFriend(applyId);
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ApiResultUtils.error(500, "添加好友失败");
        }
        return ApiResultUtils.success();
    }

    private List<FriendRequestsResultDto> getFriendApplyList(QueryWrapper<FriendRequests> wrapper) {
        List<FriendRequests> friendRequestList = friendRequestsMapper.selectList(wrapper);
        List<FriendRequestsResultDto> dtoList = new ArrayList<>();
        for (FriendRequests e : friendRequestList) {
            FriendRequestsResultDto dto = new FriendRequestsResultDto();
            BeanUtils.copyProperties(e, dto);
            dtoList.add(dto);
        }
        return dtoList;
    }
}
