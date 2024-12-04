package com.bbs.teajava.entity.dto;

import com.bbs.teajava.entity.Comments;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author kunhuang
 */
@Data
public class CommentResultDto {
    private Integer id;
    private Integer pid;
    private Integer userId;
    private String content;
    private Integer deleted;
    private LocalDateTime createAt;
    private List<CommentResultDto> children;

    public static List<CommentResultDto> convertFromComments(List<Comments> commentList) {
        List<CommentResultDto> result = new ArrayList<>();
        for (Comments comment : commentList) {
            CommentResultDto dto = new CommentResultDto();
            BeanUtils.copyProperties(comment, dto);
            result.add(dto);
        }
        return result;
    }
}
