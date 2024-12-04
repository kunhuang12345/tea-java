package com.bbs.teajava.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bbs.teajava.constants.RoleEnum;
import com.bbs.teajava.entity.Comments;
import com.bbs.teajava.entity.Papers;
import com.bbs.teajava.entity.Users;
import com.bbs.teajava.entity.dto.CommentResultDto;
import com.bbs.teajava.mapper.CommentsMapper;
import com.bbs.teajava.mapper.PapersMapper;
import com.bbs.teajava.service.ICommentsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bbs.teajava.utils.ApiResultUtils;
import com.bbs.teajava.utils.SessionUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 * 评论实现类
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
        comments.setCreateAt(LocalDateTime.now());
        commentsMapper.insert(comments);
        return ApiResultUtils.success(content);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResultUtils addChildComment(Integer paperId, Integer pid, String content) {
        Comments parentComment = commentsMapper.selectOne(new QueryWrapper<Comments>().eq("id", pid).eq("paper_id", paperId));
        if (parentComment == null) {
            return ApiResultUtils.error(500, "父评论不存在！");
        }
        Comments comments = new Comments();
        comments.setPid(pid);
        comments.setUserId(SessionUtils.getUser().getId());
        comments.setPaperId(paperId);
        comments.setContent(content);
        comments.setCreateAt(LocalDateTime.now());
        commentsMapper.insert(comments);
        return ApiResultUtils.success(content);
    }

    @Override
    public List<CommentResultDto> getAllCommentList(Integer paperId) {
        List<Comments> commentList = commentsMapper.selectListByPaperId(paperId);
        // 转换resultDto
        List<CommentResultDto> dtoList = CommentResultDto.convertFromComments(commentList);
        // 将根级评论转换为Map
        Map<Integer, CommentResultDto> dtoMap = dtoList.stream().filter(dto -> dto.getPid() == 0).collect(Collectors.toMap(CommentResultDto::getId, Function.identity()));
        for (CommentResultDto dto : dtoList) {
            if (dto.getPid() == 0 || dto.getDeleted() == 1) {
                continue;
            }
            Integer commentRootId = this.getCommentRootId(dto, dtoList);
            CommentResultDto rootDto = dtoMap.get(commentRootId);
            if (CollectionUtils.isEmpty(rootDto.getChildren())) {
                rootDto.setChildren(new ArrayList<>());
            }
            rootDto.getChildren().add(dto);
        }
        return new ArrayList<>(dtoMap.values()).stream().filter(dto -> dto.getDeleted() == 0).toList();
    }

    @Override
    public ApiResultUtils deleteComment(Integer commentId) {
        Comments comment = commentsMapper.selectOne(new QueryWrapper<Comments>().eq("id", commentId));
        Users user = SessionUtils.getUser();
        if (comment == null || (!user.getId().equals(comment.getUserId()) && user.getRole() != RoleEnum.ADMIN.getValue())) {
            return ApiResultUtils.error(500, "评论不存在或权限不足！");
        }
        int delete = commentsMapper.deleteById(commentId);
        return ApiResultUtils.success(delete);
    }

    private Integer getCommentRootId(CommentResultDto dto, List<CommentResultDto> dtoList) {
        Integer parentId = dto.getId();
        int resultId = 0;
        // 设置 <id, pid> Map
        Map<Integer, Integer> idMap = dtoList.stream().collect(Collectors.toMap(CommentResultDto::getId, CommentResultDto::getPid));
        // 获取父评论id
        while (parentId != 0) {
            resultId = parentId;
            // 获取父评论id
            parentId = idMap.get(parentId);
        }
        return resultId;
    }
}
