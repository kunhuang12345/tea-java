package com.bbs.teajava.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bbs.teajava.constants.AttachmentTagEnum;
import com.bbs.teajava.constants.BucketNameEnum;
import com.bbs.teajava.constants.PaperDownloadTypeEnum;
import com.bbs.teajava.constants.RoleEnum;
import com.bbs.teajava.entity.PaperAuthor;
import com.bbs.teajava.entity.Papers;
import com.bbs.teajava.entity.Users;
import com.bbs.teajava.entity.dto.PaperResultDto;
import com.bbs.teajava.mapper.PapersMapper;
import com.bbs.teajava.mapper.UsersMapper;
import com.bbs.teajava.service.IPaperAuthorService;
import com.bbs.teajava.service.IPaperStatusService;
import com.bbs.teajava.service.IPapersService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bbs.teajava.utils.*;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    private final IPaperStatusService paperStatusService;
    private final IPaperAuthorService paperAuthorService;
    private final UsersMapper usersMapper;

    Logger logger = LoggerFactory.getLogger(PapersServiceImpl.class);

    @Override
    public List<PaperResultDto> getAllPapers() {
        List<Papers> papersList = papersMapper.selectList(new QueryWrapper<>());
        List<PaperResultDto> dtoList = new ArrayList<>();
        for (Papers paper : papersList) {
            PaperResultDto dto = new PaperResultDto();
            BeanUtils.copyProperties(paper, dto);
            dtoList.add(dto);
        }
        return dtoList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResultUtils savePapers(Integer paperId, String title, String conference, MultipartFile paperFile, Integer attachmentTag) {
        HttpSession session = SessionUtils.getSession();
        Users user = (Users) session.getAttribute("user");
        Papers paper = new Papers();
        paper.setTitle(title);
        paper.setAuthor(user.getUsername());
        paper.setFile(String.valueOf(attachmentTag));
        paper.setConference(conference);
        paper.setReporterId(user.getId());
        if (paperId != null) {
            paper.setId(paperId);
            paper.setUpdateTime(LocalDateTime.now());
            if (paperFile != null) {
                paper.setFileSize(paperFile.getSize() + "bytes");
            }
            int updated = papersMapper.updateById(paper);
            if (updated == 0) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return ApiResultUtils.error(500, "更新失败");
            }
        } else {
            if (paperFile == null) {
                return new ApiResultUtils(500, "请上传文件");
            }
            paper.setFileSize(paperFile.getSize() + "bytes");
            paper.setCreateTime(LocalDateTime.now());
            papersMapper.insert(paper);
        }
        try {
            if (AttachmentTagEnum.EXIST.getValue() == attachmentTag) {
                // 原文件名
                String fileName = session.getAttribute("fileName").toString();
                // 临时文件名
                String attachmentTemp = FileNameUtils.attachment(fileName, session.getAttribute("randomStr").toString());
                session.removeAttribute("randomStr");
                InputStream fileStream = minio.getObject(BucketNameEnum.TEMP.getValue(), attachmentTemp);
                if (fileStream == null) {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    return ApiResultUtils.error(500, "请先上传文件");
                }
                fileStream.close();
                String attachmentPath = FilePathUtils.attachmentPath(user.getEmail(), FileNameUtils.attachment(fileName, String.valueOf(paper.getId())));
                minio.copyFile(BucketNameEnum.TEMP.getValue(), BucketNameEnum.ATTACHMENT.getValue(), attachmentTemp, attachmentPath);
                paper.setAttachmentPath(attachmentPath);
            }

            if (paperFile != null) {
                minio.uploadFile(
                        BucketNameEnum.PAPER.getValue(), // 桶名
                        FilePathUtils.paperPath(user.getEmail(), FileNameUtils.paper(paperFile.getOriginalFilename(), String.valueOf(paper.getId()))), // 文件路径
                        paperFile.getInputStream() // 文件流
                );
                paper.setPaperPath(FilePathUtils.paperPath(user.getEmail(), FileNameUtils.paper(paperFile.getOriginalFilename(), String.valueOf(paper.getId()))));
            }
            papersMapper.updateById(paper);
        } catch (Exception e) {
            logger.error("上传论文失败", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ApiResultUtils.error(500, "论文上传失败");
        }
        return ApiResultUtils.success(paper.getId());
    }

    @Override
    public ApiResultUtils uploadTempFile(MultipartFile attachment) {
        try {
            TempFileUploadUtil.upload(minio, attachment);
            return ApiResultUtils.success();
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
        Users user = (Users) session.getAttribute("user");
        if (user.getRole() != RoleEnum.ADMIN.getValue() && !user.getId().equals(paper.getReporterId())) {
            return ApiResultUtils.error(403, "无权限删除");
        }
        try {
            minio.deleteFile(BucketNameEnum.PAPER.getValue(), paper.getPaperPath());
            if (paper.getAttachmentPath() != null) {
                minio.deleteFile(BucketNameEnum.ATTACHMENT.getValue(), paper.getAttachmentPath());
            }
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

    @Override
    public void download(Integer paperId, Integer type) throws Exception {
        Papers papers = papersMapper.selectById(paperId);
        if (papers == null) {
            throw new Exception("论文不存在");
        }
        try {
            PaperDownloadTypeEnum typeEnum = PaperDownloadTypeEnum.getEnum(type);
            String bucketName;
            String filePath;
            if (typeEnum.getValue() == PaperDownloadTypeEnum.PAPER.getValue()) {
                bucketName = BucketNameEnum.PAPER.getValue();
                filePath = papers.getPaperPath();
            } else {
                bucketName = BucketNameEnum.ATTACHMENT.getValue();
                filePath = papers.getAttachmentPath();
            }
            if (type == PaperDownloadTypeEnum.ATTACHMENT.getValue()) {
                // 更新附件下载量
                paperStatusService.addAttachmentDownloadCount(paperId);
            }
            minio.downLoadFile(bucketName, filePath);
        } catch (Exception e) {
            logger.error("下载论文失败", e);
            throw new Exception("下载失败");
        }
    }

    @Override
    public PaperResultDto getPaperById(Integer paperId) {
        Papers paper = papersMapper.selectById(paperId);
        if (paper != null) {
            PaperResultDto dto = new PaperResultDto();
            BeanUtils.copyProperties(paper, dto);
            return dto;
        }
        return null;
    }

    @Override
    public IPage<PaperResultDto> getPaperListByPage(Integer page, Integer pageSize) {
        return this.getPaperListByPage(page, pageSize, new QueryWrapper<>());
    }

    @Override
    public IPage<PaperResultDto> getUserPaperListByPage(Integer page, Integer pageSize, Integer userId) {
        List<PaperAuthor> paperAuthorList = paperAuthorService.list(new QueryWrapper<PaperAuthor>().eq("user_id", userId));
        List<Integer> list = paperAuthorList.stream().filter(Objects::nonNull).map(PaperAuthor::getPaperId).toList();
        return this.getPaperListByPage(page, pageSize, new QueryWrapper<Papers>().in("id", list));
    }

    @Override
    public ApiResultUtils attachAuthor(Integer paperId, String ids) {
        Users user = usersMapper.selectById(ids);
        if (user == null) {
            return ApiResultUtils.error(500, "用户不存在");
        }
        Papers paper = papersMapper.selectById(paperId);
        paper.setAttachAuthor(ids);
        int update = papersMapper.updateById(paper);
        return ApiResultUtils.success(update);
    }

    private IPage<PaperResultDto> getPaperListByPage(Integer page, Integer pageSize, QueryWrapper<Papers> queryWrapper) {
        Page<Papers> papersPage = papersMapper.selectPage(new Page<>(page, pageSize), queryWrapper);
        Page<PaperResultDto> dtoPage = new Page<>();
        BeanUtils.copyProperties(papersPage, dtoPage);
        List<PaperResultDto> dtoList = new ArrayList<>();
        for (Papers paper : papersPage.getRecords()) {
            PaperResultDto dto = new PaperResultDto();
            BeanUtils.copyProperties(paper, dto);
            dtoList.add(dto);
        }
        dtoPage.setRecords(dtoList);
        return dtoPage;
    }
}
