package com.bbs.teajava.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serial;
import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * <p>
 * 添加好友信息类
 * </p>
 *
 * @author hk
 * @since 2024-11-30
 */
@TableName("friend_requests")
@Data
public class FriendRequests implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 申请人ID
     */
    @TableField("from_user_id")
    private Integer fromUserId;

    /**
     * 接收人ID
     */
    @TableField("to_user_id")
    private Integer toUserId;

    /**
     * 状态：0-待处理 1-已接受 2-已拒绝
     */
    @TableField("status")
    private Integer status;

    /**
     * 申请消息
     */
    @TableField("message")
    private String message;

    @TableField("create_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;


    @Override
    public String toString() {
        return "FriendRequests{" +
            "id=" + id +
            ", fromUserId=" + fromUserId +
            ", toUserId=" + toUserId +
            ", status=" + status +
            ", message=" + message +
            ", createTime=" + createTime +
        "}";
    }
}
