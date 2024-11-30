package com.bbs.teajava.entity;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

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
@TableName("papers")
@Data
public class Papers implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String title;

    private String author;

    private String conference;

    private String file;

    private Integer reporterId;

    private String paperPath;

    private String attachmentPath;

    @TableLogic
    private Integer deleted;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime deleteTime;

    @Override
    public String toString() {
        return "Papers{" +
            "id=" + id +
            ", title=" + title +
            ", author=" + author +
            ", conference=" + conference +
            ", file=" + file +
            ", reporterId=" + reporterId +
        "}";
    }
}
