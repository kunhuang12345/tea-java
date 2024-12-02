package com.bbs.teajava.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bbs.teajava.entity.PaperStatus;
import com.bbs.teajava.entity.Papers;
import com.bbs.teajava.mapper.PaperStatusMapper;
import com.bbs.teajava.mapper.PapersMapper;
import com.bbs.teajava.service.IPaperStatusService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bbs.teajava.utils.ApiResultUtils;
import com.bbs.teajava.utils.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 论文状态统计表 服务实现类
 * </p>
 *
 * @author hk
 * @since 2024-12-02
 */
@Service
@RequiredArgsConstructor
public class PaperStatusServiceImpl extends ServiceImpl<PaperStatusMapper, PaperStatus> implements IPaperStatusService {

    private final RedisUtil redisUtil;
    private final PapersMapper papersMapper;
    private final PaperStatusMapper paperStatusMapper;

    @Override
    public ApiResultUtils getViewCount(Integer paperId) {
        String count = redisUtil.get("viewCount" + paperId);
        return new ApiResultUtils(200, count, "success");
    }

    @Override
    public ApiResultUtils getDownloadCount(Integer paperId) {
        String count = redisUtil.get("downloadCount" + paperId);
        return new ApiResultUtils(200, count, "success");
    }

    @Override
    public ApiResultUtils getAttachmentDownloadCount(Integer paperId) {
        String count = redisUtil.get("attachmentDownloadCount" + paperId);
        return new ApiResultUtils(200, count, "success");
    }

    @Override
    public ApiResultUtils addViewCount(Integer paperId) {
        String count = redisUtil.get("viewCount" + paperId);
        if (count == null) {
            Papers papers = papersMapper.selectById(paperId);
            if (papers == null) {
                return new ApiResultUtils(400, "论文不存在", "error");
            }
            count = "0";
        }
        redisUtil.set("viewCount" + paperId, String.valueOf(Integer.parseInt(count) + 1));
        if ((Integer.parseInt(count) + 1) % 100 == 0) {
            PaperStatus paperStatus = paperStatusMapper.selectOne(new QueryWrapper<PaperStatus>().eq("paper_id", paperId));
            if (paperStatus == null) {
                paperStatus = new PaperStatus();
                paperStatus.setPaperId(paperId);
                paperStatus.setReadCount(100);
                paperStatus.setDownloadCount(0);
                paperStatus.setAttachmentDownloadCount(0);
                paperStatusMapper.insert(paperStatus);
            } else {
                paperStatus.setReadCount(paperStatus.getReadCount() + 100);
                paperStatusMapper.updateById(paperStatus);
            }
        }
        return new ApiResultUtils(200, "success");
    }

    @Override
    public ApiResultUtils addDownloadCount(Integer paperId) {
        String count = redisUtil.get("downloadCount" + paperId);
        if (count == null) {
            Papers papers = papersMapper.selectById(paperId);
            if (papers == null) {
                return new ApiResultUtils(400, "论文不存在", "error");
            }
            count = "0";
        }
        redisUtil.set("downloadCount" + paperId, String.valueOf(Integer.parseInt(count) + 1));
        if ((Integer.parseInt(count) + 1) % 100 == 0) {
            PaperStatus paperStatus = paperStatusMapper.selectOne(new QueryWrapper<PaperStatus>().eq("paper_id", paperId));
            if (paperStatus == null) {
                paperStatus = new PaperStatus();
                paperStatus.setPaperId(paperId);
                paperStatus.setReadCount(0);
                paperStatus.setDownloadCount(100);
                paperStatus.setAttachmentDownloadCount(0);
                paperStatusMapper.insert(paperStatus);
            } else {
                paperStatus.setDownloadCount(paperStatus.getDownloadCount() + 100);
                paperStatusMapper.updateById(paperStatus);
            }
        }
        return new ApiResultUtils(200, "success");
    }

    @Override
    public ApiResultUtils addAttachmentDownloadCount(Integer paperId) {
        String count = redisUtil.get("attachmentDownloadCount" + paperId);
        if (count == null) {
            Papers papers = papersMapper.selectById(paperId);
            if (papers == null) {
                return new ApiResultUtils(400, "论文不存在", "error");
            }
            count = "0";
        }
        redisUtil.set("attachmentDownloadCount" + paperId, String.valueOf(Integer.parseInt(count) + 1));
        if ((Integer.parseInt(count) + 1) % 100 == 0) {
            PaperStatus paperStatus = paperStatusMapper.selectOne(new QueryWrapper<PaperStatus>().eq("paper_id", paperId));
            if (paperStatus == null) {
                paperStatus = new PaperStatus();
                paperStatus.setPaperId(paperId);
                paperStatus.setReadCount(0);
                paperStatus.setDownloadCount(0);
                paperStatus.setAttachmentDownloadCount(100);
                paperStatusMapper.insert(paperStatus);
            } else {
                paperStatus.setAttachmentDownloadCount(paperStatus.getAttachmentDownloadCount() + 100);
                paperStatusMapper.updateById(paperStatus);
            }
        }
        return new ApiResultUtils(200, "success");
    }
}
