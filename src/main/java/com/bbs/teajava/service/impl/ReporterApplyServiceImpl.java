package com.bbs.teajava.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bbs.teajava.constants.AttachmentTagEnum;
import com.bbs.teajava.constants.BucketNameEnum;
import com.bbs.teajava.constants.ReporterApplyStatusEnum;
import com.bbs.teajava.entity.ReporterApply;
import com.bbs.teajava.entity.Users;
import com.bbs.teajava.mapper.ReporterApplyMapper;
import com.bbs.teajava.service.IReporterApplyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bbs.teajava.service.IUsersService;
import com.bbs.teajava.utils.*;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author hk
 * @since 2024-11-30
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ReporterApplyServiceImpl extends ServiceImpl<ReporterApplyMapper, ReporterApply> implements IReporterApplyService {

    private final ReporterApplyMapper reporterApplyMapper;
    private final MinioUtil minio;
    private final IUsersService usersService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResultUtils reporterRegister(String applyNote, Integer fileTag) {
        Users user = SessionUtils.getUser();
        QueryWrapper<ReporterApply> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", user.getId());
        ReporterApply reporterApply = reporterApplyMapper.selectOne(queryWrapper);
        if (reporterApply == null) {
            reporterApply = new ReporterApply();
        }
        reporterApply.setUserId(user.getId());
        reporterApply.setApplyNote(applyNote);
        reporterApply.setApplyTime(LocalDateTime.now());
        reporterApply.setStatus(ReporterApplyStatusEnum.WAIT_AUDIT.getValue());
        if (fileTag == AttachmentTagEnum.EXIST.getValue()) {
            HttpSession session = SessionUtils.getSession();
            String randomStr = (String) session.getAttribute("randomStr");
            String fileName = (String) session.getAttribute("fileName");
            session.removeAttribute("randomStr");
            InputStream fileStream = minio.getObject(BucketNameEnum.TEMP.getValue(), FileNameUtils.attachment(fileName, randomStr));
            if (fileStream == null) {
                return ApiResultUtils.error(500, "文件不存在");
            }
            try {
                fileStream.close();
            } catch (IOException e) {
                log.error("关闭文件流失败", e);
            }
            String filePath = FilePathUtils.reporterApply(user.getEmail(), fileName);
            reporterApply.setFilePath(filePath);
            minio.copyFile(BucketNameEnum.TEMP.getValue(), BucketNameEnum.REPORTER.getValue(), FileNameUtils.attachment(fileName, randomStr), filePath);
        }
        if (reporterApply.getId() == null) {
            reporterApplyMapper.insert(reporterApply);
        } else {
            reporterApplyMapper.updateById(reporterApply);
        }
        return ApiResultUtils.success();
    }


    @Override
    public ApiResultUtils uploadTempFile(MultipartFile attachment) {
        try {
            TempFileUploadUtil.upload(minio, attachment);
            return ApiResultUtils.success();
        } catch (Exception e) {
            log.error("临时文件上传失败", e);
            return ApiResultUtils.error(500, "临时文件上传失败");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResultUtils approveRegister(Integer id, Integer status) {
        // TODO: 修改对方session状态
        ReporterApply reporterApply = reporterApplyMapper.selectById(id);
        if (reporterApply == null) {
            return ApiResultUtils.error(404, "记录不存在");
        }
        try {
            ReporterApplyStatusEnum statusEnum = ReporterApplyStatusEnum.getEnum(status);
            reporterApply.setStatus(statusEnum.getValue());
            reporterApplyMapper.updateById(reporterApply);
            usersService.reporterRegister(reporterApply.getUserId());
        } catch (Exception e) {
            log.error("审批状态不正确", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ApiResultUtils.error(400, "审批状态不正确");
        }
        return ApiResultUtils.success();
    }

    @Override
    public List<ReporterApply> getAllRegisterList() {
        return reporterApplyMapper.selectList(null);
    }
}
