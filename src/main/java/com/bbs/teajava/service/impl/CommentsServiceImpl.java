package com.bbs.teajava.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bbs.teajava.entity.Comments;
import com.bbs.teajava.entity.Papers;
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
        int insert = commentsMapper.insert(comments);
        return ApiResultUtils.success(insert);
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
        int insert = commentsMapper.insert(comments);
        return ApiResultUtils.success(insert);
    }

    @Override
    public List<CommentResultDto> getAllCommentList(Integer paperId) {
        List<Comments> commentList = commentsMapper.selectList(new QueryWrapper<Comments>().eq("paper_id", paperId));
        // 转换resultDto
        List<CommentResultDto> dtoList = CommentResultDto.convertFromComments(commentList);
        // 将根级评论转换为Map
        Map<Integer, CommentResultDto> dtoMap = dtoList.stream().filter(dto -> dto.getPid() == 0).collect(Collectors.toMap(CommentResultDto::getId, Function.identity()));
        for (CommentResultDto dto : dtoList) {
            if (dto.getPid() == 0) {
                continue;
            }
            Integer commentRootId = this.getCommentRootId(dto, dtoList);
            CommentResultDto rootDto = dtoMap.get(commentRootId);
            if (CollectionUtils.isEmpty(rootDto.getChildren())) {
                rootDto.setChildren(new ArrayList<>());
            }
            rootDto.getChildren().add(dto);
        }
        return new ArrayList<>(dtoMap.values());
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
