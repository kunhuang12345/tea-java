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
@TableName("papers")
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
    public String getConference() {
        return conference;
    }

    public void setConference(String conference) {
        this.conference = conference;
    }
    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }
    public Integer getReporterId() {
        return reporterId;
    }

    public void setReporterId(Integer reporterId) {
        this.reporterId = reporterId;
    }

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
