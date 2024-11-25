package com.bbs.teajava.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serial;
import java.io.Serializable;


/**
 * <p>
 * 
 * </p>
 *
 * @author hk
 * @since 2024-11-25
 */
@TableName("friends")
public class Friends implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "user_id", type = IdType.AUTO)
    private Integer userId;

    private Integer friendId;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    public Integer getFriendId() {
        return friendId;
    }

    public void setFriendId(Integer friendId) {
        this.friendId = friendId;
    }

    @Override
    public String toString() {
        return "Friends{" +
            "userId=" + userId +
            ", friendId=" + friendId +
        "}";
    }
}
