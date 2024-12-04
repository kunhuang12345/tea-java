package com.bbs.teajava.entity.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author kunhuang
 */

@Data
public class PaperResultDto {

    private Integer id;

    private String title;

    private String author;

    private String conference;

    private String file;

    private String fileSize;

    private String paperPath;

    private String attachmentPath;

    private String attachAuthor;

    private LocalDateTime createTime;
}
