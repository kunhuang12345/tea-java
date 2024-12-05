package com.bbs.teajava.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author kunhuang
 */
@TableName("paper_author")
@Data
public class PaperAuthor {
    @TableId(type = IdType.AUTO)
    private int id;
    @TableField("paper_id")
    private int paperId;
    @TableField("user_id")
    private int userId;
}
