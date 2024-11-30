package com.bbs.teajava.entity.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * @author kunhuang
 */
@Data
public class FriendRequestsResultDto {
    /**
     * 申请人ID
     */
    private Integer fromUserId;

    /**
     * 接收人ID
     */
    private Integer toUserId;

    /**
     * 状态：0-待处理 1-已接受 2-已拒绝
     */
    private Integer status;

    /**
     * 申请消息
     */
    private String message;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
}
