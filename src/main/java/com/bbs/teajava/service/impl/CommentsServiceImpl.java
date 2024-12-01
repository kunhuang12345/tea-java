package com.bbs.teajava.service.impl;

import com.bbs.teajava.entity.Comments;
import com.bbs.teajava.entity.Papers;
import com.bbs.teajava.mapper.CommentsMapper;
import com.bbs.teajava.mapper.PapersMapper;
import com.bbs.teajava.service.ICommentsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bbs.teajava.utils.ApiResultUtils;
import com.bbs.teajava.utils.SessionUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 *  评论实现类
 * </p>
 *
 * @author hk
 * @since 2024-11-25
 */
@Service
@RequiredArgsConstructor
public class CommentsServiceImpl extends ServiceImpl<CommentsMapper, Comments> implements ICommentsService {

    private final CommentsMapper commentsMapper;
    private final PapersMapper papersMapper;
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResultUtils addPaperComment(Integer paperId, String content) {
        Papers paper = papersMapper.selectById(paperId);
        if (paper == null) {
            return ApiResultUtils.error(500, "论文不存在！");
        }
        Comments comments = new Comments();
        comments.setPid(0);
        comments.setUserId(SessionUtils.getUser().getId());
        comments.setPaperId(paperId);
        comments.setContent(content);
        int insert = commentsMapper.insert(comments);
        return ApiResultUtils.success(insert);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResultUtils addChildComment(Integer pid, String content) {
        Comments parentComment = commentsMapper.selectById(pid);
        if (parentComment == null) {
            return ApiResultUtils.error(500, "父评论不存在！");
        }
        Comments comments = new Comments();
        comments.setPid(pid);
        comments.setUserId(SessionUtils.getUser().getId());
        comments.setPaperId(0);
        comments.setContent(content);
        int insert = commentsMapper.insert(comments);
        return ApiResultUtils.success(insert);
    }
}
