package com.bbs.teajava.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * <p>
 * 论文状态统计表
 * </p>
 *
 * @author hk
 * @since 2024-12-02
 */
@TableName("paper_status")
@Data
public class PaperStatus implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 论文ID
     */
    @TableField("paper_id")
    private Integer paperId;

    /**
     * 论文下载量
     */
    @TableField("download_count")
    private Integer downloadCount;

    /**
     * 论文附件下载量
     */
    @TableField("attachment_download_count")
    private Integer attachmentDownloadCount;

    /**
     * 论文阅读量
     */
    @TableField("read_count")
    private Integer readCount;


}
