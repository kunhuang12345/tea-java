package com.bbs.teajava.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author hk
 * @since 2024-11-25
 */
@TableName("comments")
@Data
public class Comments implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer pid;

    private Integer userId;

    private Integer paperId;

    private String content;

    @TableField("create_at")
    private LocalDateTime createAt;

    @TableLogic
    private Integer deleted;

}
