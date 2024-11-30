package com.bbs.teajava.entity;

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
 * 
 * </p>
 *
 * @author hk
 * @since 2024-11-30
 */
@TableName("reporter_apply")
@Data
public class ReporterApply implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer userId;

    private String applyNote;

    private String filePath;

    private Integer status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime applyTime;

    @Override
    public String toString() {
        return "ReporterApply{" +
            "id=" + id +
            ", userId=" + userId +
            ", applyNote=" + applyNote +
            ", filePath=" + filePath +
            ", applyTime=" + applyTime +
        "}";
    }
}
