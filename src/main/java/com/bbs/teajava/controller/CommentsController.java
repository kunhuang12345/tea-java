package com.bbs.teajava.controller;


import com.bbs.teajava.annotation.Authentication;
import com.bbs.teajava.annotation.MuteCheck;
import com.bbs.teajava.service.ICommentsService;
import com.bbs.teajava.utils.ApiResultUtils;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 评论接口
 * </p>
 *
 * @author hk
 * @since 2024-11-25
 */
@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentsController {

    private final ICommentsService commentsService;

    @RequestMapping(value = "AddPaperComment", method = {RequestMethod.POST})
    @ApiOperation("添加论文评论")
    @Authentication
    @MuteCheck
    public ApiResultUtils addPaperComment(@RequestParam(value = "paperId") Integer paperId,
                                          @RequestParam(value = "content") String content) {
        return commentsService.addPaperComment(paperId, content);
    }

    @RequestMapping(value = "AddChildComment", method = {RequestMethod.POST})
    @ApiOperation("添加子评论")
    @Authentication
    @MuteCheck
    public ApiResultUtils addChildComment(@RequestParam(value = "paperId") Integer paperId,
                                          @RequestParam(value = "pid") Integer pid,
                                          @RequestParam(value = "content") String content) {
        return commentsService.addChildComment(paperId, pid, content);
    }

    @RequestMapping(value = "GetAllCommentList", method = {RequestMethod.GET, RequestMethod.POST})
    @ApiOperation("获取评论列表")
    public ApiResultUtils getAllCommentList(@RequestParam(value = "paperId") Integer paperId) {
        return ApiResultUtils.success(commentsService.getAllCommentList(paperId));
    }

    @RequestMapping(value = "DeleteComment", method = {RequestMethod.POST})
    @ApiOperation("删除评论")
    @Authentication
    public ApiResultUtils deleteComment(@RequestParam(value = "commentId") Integer commentId) {
        return commentsService.deleteComment(commentId);
    }


}
