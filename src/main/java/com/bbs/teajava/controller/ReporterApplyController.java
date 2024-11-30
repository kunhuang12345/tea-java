package com.bbs.teajava.controller;


import com.bbs.teajava.annotation.Authentication;
import com.bbs.teajava.service.IReporterApplyService;
import com.bbs.teajava.utils.ApiResultUtils;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 会议报告人申请表接口
 * </p>
 *
 * @author hk
 * @since 2024-11-30
 */
@RestController
@RequestMapping("/reporter-apply")
@RequiredArgsConstructor
public class ReporterApplyController {
    private final IReporterApplyService reporterApplyService;


    @RequestMapping(value = "ReporterRegister", method = {RequestMethod.POST})
    @ApiResponse(description = "会议报告人注册申请 fileTag: 0 没有附件 1 有附件")
    @Authentication
    public ApiResultUtils reporterRegister(@RequestParam(value = "applyNote") String applyNote,
                                           @RequestParam(value = "fileTag") Integer fileTag) {
        return reporterApplyService.reporterRegister(applyNote, fileTag);
    }
    
    @RequestMapping(value = "ApproveRegister", method = {RequestMethod.POST})
    @ApiResponse(description = "审核会议报告人注册申请")
    @Authentication(requireAdmin = true)
    public ApiResultUtils approveRegister(@RequestParam(value = "id") Integer id) {
        return reporterApplyService.approveRegister(id);
    }

    @RequestMapping(value = "UploadTempFile", method = {RequestMethod.POST})
    @ApiResponse(description = "上传临时文件")
    @Authentication
    public ApiResultUtils uploadTempFile(@RequestParam(value = "attachment") MultipartFile attachment) {
        return reporterApplyService.uploadTempFile(attachment);
    }

}
