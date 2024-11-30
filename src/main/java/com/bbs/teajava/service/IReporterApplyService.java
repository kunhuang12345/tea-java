package com.bbs.teajava.service;

import com.bbs.teajava.entity.ReporterApply;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bbs.teajava.utils.ApiResultUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author hk
 * @since 2024-11-30
 */
public interface IReporterApplyService extends IService<ReporterApply> {

    /**
     * 会议报告人注册申请
     *
     * @param applyNote 申请理由
     * @param fileTag 文件标志
     * @return 注册结果
     */
    ApiResultUtils reporterRegister(String applyNote, Integer fileTag);

    /**
     * 上传临时文件
     *
     * @param attachment 临时文件
     * @return 上传结果
     */
    ApiResultUtils uploadTempFile(MultipartFile attachment);

    /**
     * 审核会议报告人注册申请
     *
     * @param id 申请人id
     * @return 审核结果
     */
    ApiResultUtils approveRegister(Integer id);
}
