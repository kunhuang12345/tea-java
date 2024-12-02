package com.bbs.teajava.service;

import com.bbs.teajava.entity.Papers;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bbs.teajava.entity.dto.PaperResultDto;
import com.bbs.teajava.utils.ApiResultUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author hk
 * @since 2024-11-25
 */
public interface IPapersService extends IService<Papers> {

    /**
     * 获取所有论文
     *
     * @return 所有论文列表
     */
    List<PaperResultDto> getAllPapers();

    /**
     * 新增/修改论文
     *
     * @param paperId       论文id
     * @param title         论文标题
     * @param conference    论文模块
     * @param paperFile     论文文件
     * @param attachmentTag 附件标记
     * @return 上传结果
     */
    ApiResultUtils savePapers(Integer paperId, String title, String conference, MultipartFile paperFile, Integer attachmentTag);

    /**
     * 上传文件 测试
     *
     * @param attachment 附件
     * @return 存储结果
     */
    ApiResultUtils uploadTempFile(MultipartFile attachment);

    /**
     * 删除论文
     *
     * @param paperId 论文id
     * @return 删除结果
     */
    ApiResultUtils deletePaper(Integer paperId);

    /**
     * 清理已删除数据
     */
    void cleanDeletedData();

    /**
     * 下载论文
     *
     * @param paperId 论文id
     * @param type 0:论文, 1:论文附件
     */
    void download(Integer paperId, Integer type) throws Exception;

    /**
     * 根据id获取论文信息
     *
     * @param paperId 论文id
     * @return 论文信息
     */
    PaperResultDto getPaperById(Integer paperId);
}
