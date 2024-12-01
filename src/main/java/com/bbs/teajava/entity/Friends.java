package com.bbs.teajava.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;


/**
 * <p>
 * 好友类
 * </p>
 *
 * @author hk
 * @since 2024-11-25
 */
@TableName("friends")
@Data
public class Friends implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Integer userId;

    private Integer friendId;

    private Integer isFriend;

    private Integer isFollow;

}
