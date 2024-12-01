package com.bbs.teajava.service;

import com.bbs.teajava.entity.Comments;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bbs.teajava.entity.dto.CommentResultDto;
import com.bbs.teajava.utils.ApiResultUtils;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author hk
 * @since 2024-11-25
 */
public interface ICommentsService extends IService<Comments> {

    /**
     * 添加论文评论
     *
     * @param paperId 论文id
     * @param content 评论内容
     * @return 添加结果
     */
    ApiResultUtils addPaperComment(Integer paperId, String content);

    /**
     * 添加子评论
     *
     * @param paperId
     * @param pid     父评论id
     * @param content 子评论内容
     * @return 添加结果
     */
    ApiResultUtils addChildComment(Integer paperId, Integer pid, String content);

    /**
     * 获取论文评论列表
     *
     * @param paperId 论文id
     * @return 评论列表
     */
    List<CommentResultDto> getAllCommentList(Integer paperId);

    /**
     * 删除评论
     *
     * @param commentId 评论id
     * @return 删除结果
     */
    ApiResultUtils deleteComment(Integer commentId);
}
