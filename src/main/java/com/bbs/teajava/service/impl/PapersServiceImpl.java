package com.bbs.teajava.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bbs.teajava.constants.AttachmentTagEnum;
import com.bbs.teajava.constants.BucketNameEnum;
import com.bbs.teajava.constants.RoleEnum;
import com.bbs.teajava.entity.Papers;
import com.bbs.teajava.entity.Users;
import com.bbs.teajava.mapper.PapersMapper;
import com.bbs.teajava.service.IPapersService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bbs.teajava.utils.*;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Random;

/**
 * <p>
 * 论文实现类
 * </p>
 *
 * @author hk
 * @since 2024-11-25
 */
@Service
@RequiredArgsConstructor
public class PapersServiceImpl extends ServiceImpl<PapersMapper, Papers> implements IPapersService {
    private final PapersMapper papersMapper;
    private final MinioUtil minio;

    Logger logger = LoggerFactory.getLogger(PapersServiceImpl.class);

    @Override
    public List<Papers> getAllPapers() {
        return papersMapper.selectList(new QueryWrapper<>());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResultUtils savePapers(Integer paperId, String title, String conference, MultipartFile paperFile, Integer attachmentTag) {
        HttpSession session = SessionUtils.getSession();
        Users user = (Users) session.getAttribute(session.getId());
        Papers paper = new Papers();
        paper.setTitle(title);
        paper.setAuthor(user.getUsername());
        paper.setFile(String.valueOf(attachmentTag));
        paper.setConference(conference);
        paper.setReporterId(user.getId());
        if (paperId != null) {
            paper.setId(paperId);
            int updated = papersMapper.updateById(paper);
            if (updated == 0) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return ApiResultUtils.error(500, "更新失败");
            }
        } else {
            papersMapper.insert(paper);
        }
        if (AttachmentTagEnum.EXIST.getValue() == attachmentTag) {
            // 原文件名
            String fileName = session.getAttribute("fileName").toString();
            // 临时文件名
            String attachmentTemp = FileNameUtils.attachment(fileName, session.getAttribute("randomStr").toString());
            session.removeAttribute("randomString");
            InputStream fileStream = minio.getObject(BucketNameEnum.TEMP.getValue(), attachmentTemp);
            if (fileStream == null) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return ApiResultUtils.error(500, "请先上传文件");
            }
            String attachmentPath = FilePathUtils.attachmentPath(user.getEmail(), FileNameUtils.attachment(fileName, String.valueOf(paper.getId())));
            minio.copyFile(BucketNameEnum.TEMP.getValue(), BucketNameEnum.ATTACHMENT.getValue(), attachmentTemp, attachmentPath);
            paper.setAttachmentPath(attachmentPath);
        }
        try {
            minio.uploadFile(
                    BucketNameEnum.PAPER.getValue(), // 桶名
                    FilePathUtils.paperPath(user.getEmail(), FileNameUtils.paper(paperFile.getOriginalFilename(), String.valueOf(paper.getId()))), // 文件路径
                    paperFile.getInputStream() // 文件流
            );
            paper.setPaperPath(FilePathUtils.paperPath(user.getEmail(), FileNameUtils.paper(paperFile.getOriginalFilename(), String.valueOf(paper.getId()))));
            papersMapper.updateById(paper);
        } catch (IOException e) {
            logger.error("上传论文失败", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ApiResultUtils.error(500, "论文上传失败");
        }
        return ApiResultUtils.success();
    }

    @Override
    public ApiResultUtils uploadTempFile(MultipartFile attachment) {
        try {
            HttpSession session = SessionUtils.getSession();
            Random random = RandomUtil.getRandom();
            int randomNum = random.nextInt(10000000);
            String randomStr = String.format("%08d", randomNum);
            session.setAttribute("randomStr", randomStr);
            session.setAttribute("fileName", attachment.getOriginalFilename());
            minio.uploadFile(BucketNameEnum.TEMP.getValue(), FileNameUtils.attachment(attachment.getOriginalFilename(), randomStr), attachment.getInputStream());
            return ApiResultUtils.success("上传成功");
        } catch (IOException e) {
            return ApiResultUtils.error(500, "上传失败");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResultUtils deletePaper(Integer paperId) {
        HttpSession session = SessionUtils.getSession();
        Papers paper = papersMapper.selectById(paperId);
        if (paper == null) {
            return ApiResultUtils.error(404, "论文不存在");
        }
        Users user = (Users) session.getAttribute(session.getId());
        if (user.getRole() != RoleEnum.ADMIN.getValue() && !user.getId().equals(paper.getReporterId())) {
            return ApiResultUtils.error(403, "无权限删除");
        }
        try {
            minio.deleteFile(BucketNameEnum.PAPER.getValue(), paper.getPaperPath());
            minio.deleteFile(BucketNameEnum.ATTACHMENT.getValue(), paper.getAttachmentPath());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ApiResultUtils.error(500, "删除失败");
        }
        int deleted = papersMapper.delete(new QueryWrapper<Papers>().eq("id", paperId));
        return ApiResultUtils.success(deleted);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cleanDeletedData() {
        papersMapper.clean();
    }
}
