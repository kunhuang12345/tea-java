package com.bbs.teajava.controller;


import com.bbs.teajava.annotation.Authentication;
import com.bbs.teajava.service.IPaperStatusService;
import com.bbs.teajava.utils.ApiResultUtils;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 论文状态统计表 前端控制器
 * </p>
 *
 * @author hk
 * @since 2024-12-02
 */
@RestController
@RequestMapping("/PaperStatus")
@RequiredArgsConstructor
public class PaperStatusController {

    private final IPaperStatusService paperStatusService;

    @RequestMapping(value = "GetViewCount", method = {RequestMethod.GET, RequestMethod.POST})
    @ApiOperation("获取论文下载量查看论文阅读量")
    public ApiResultUtils getViewCount(@RequestParam(value = "paperId") Integer paperId) {
        return paperStatusService.getViewCount(paperId);
    }

    @RequestMapping(value = "GetDownloadCount", method = {RequestMethod.GET, RequestMethod.POST})
    @ApiOperation("获取论文下载量")
    public ApiResultUtils getDownloadCount(@RequestParam(value = "paperId") Integer paperId) {
        return paperStatusService.getDownloadCount(paperId);
    }

    @RequestMapping(value = "GetAttachmentDownloadCount", method = {RequestMethod.GET, RequestMethod.POST})
    @ApiOperation("获取论文附件下载量")
    public ApiResultUtils getAttachmentDownloadCount(@RequestParam(value = "paperId") Integer paperId) {
        return paperStatusService.getAttachmentDownloadCount(paperId);
    }

    @RequestMapping(value = "AddViewCount", method = {RequestMethod.POST})
    @ApiOperation("论文阅读量+1")
    public ApiResultUtils addViewCount(@RequestParam(value = "paperId") Integer paperId) {
        return paperStatusService.addViewCount(paperId);
    }

    @RequestMapping(value = "AddDownloadCount", method = {RequestMethod.POST})
    @ApiOperation("论文下载量+1")
    public ApiResultUtils addDownloadCount(@RequestParam(value = "paperId") Integer paperId) {
        return paperStatusService.addDownloadCount(paperId);
    }
}
