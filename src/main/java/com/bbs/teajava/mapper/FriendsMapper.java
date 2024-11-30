package com.bbs.teajava.mapper;

import com.bbs.teajava.entity.Friends;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author hk
 * @since 2024-11-25
 */
public interface FriendsMapper extends BaseMapper<Friends> {

    /**
     * 批量插入
     *
     * @param friendList 好友数据列表
     * @return 影响行数
     */
    int batchInsert(List<Friends> friendList);
}
