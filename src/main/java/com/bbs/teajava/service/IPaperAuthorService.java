package com.bbs.teajava.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bbs.teajava.entity.PaperAuthor;
import com.bbs.teajava.utils.ApiResultUtils;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author hk
 * @since 2024-11-30
 */
public interface IPaperAuthorService extends IService<PaperAuthor> {


    /**
     * 添加论文作者
     *
     * @param paperId 论文id
     * @param userIds 用户id
     * @return 添加结果
     */
    ApiResultUtils add(Integer paperId, String userIds);
}
