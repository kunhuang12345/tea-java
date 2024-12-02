package com.bbs.teajava.service;

import com.bbs.teajava.entity.PaperStatus;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bbs.teajava.utils.ApiResultUtils;

/**
 * <p>
 * 论文状态统计表 服务类
 * </p>
 *
 * @author hk
 * @since 2024-12-02
 */
public interface IPaperStatusService extends IService<PaperStatus> {

    /**
     * 获取论文阅读量
     *
     * @param paperId 论文ID
     * @return 论文阅读量
     */
    ApiResultUtils getViewCount(Integer paperId);

    /**
     * 获取论文下载量
     *
     * @param paperId 论文ID
     * @return 论文下载量
     */
    ApiResultUtils getDownloadCount(Integer paperId);

    /**
     * 获取论文附件下载量
     *
     * @param paperId 论文ID
     * @return +1 结果
     */
    ApiResultUtils getAttachmentDownloadCount(Integer paperId);

    /**
     * 论文阅读量+1
     *
     * @param paperId 论文ID
     * @return +1 结果
     */
    ApiResultUtils addViewCount(Integer paperId);

    /**
     * 论文下载量+1
     *
     * @param paperId 论文ID
     * @return +1 结果
     */
    ApiResultUtils addDownloadCount(Integer paperId);

    /**
     * 论文附件下载量+1
     *
     * @param paperId 论文ID
     * @return +1 结果
     */
    ApiResultUtils addAttachmentDownloadCount(Integer paperId);
}
