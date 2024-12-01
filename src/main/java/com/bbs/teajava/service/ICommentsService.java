package com.bbs.teajava.service;

import com.bbs.teajava.entity.Comments;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bbs.teajava.utils.ApiResultUtils;

/**
 * <p>
 *  服务类
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
     * @param pid 父评论id
     * @param content 子评论内容
     * @return 添加结果
     */
    ApiResultUtils addChildComment(Integer pid, String content);
}
