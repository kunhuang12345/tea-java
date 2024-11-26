package com.bbs.teajava.service;

import com.bbs.teajava.entity.Papers;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author hk
 * @since 2024-11-25
 */
public interface IPapersService extends IService<Papers> {

    /**
     * 获取所有论文
     * @return
     */
    List<Papers> getAllPapers();

    /**
     * 上传论文
     * @param papers
     * @return
     */
    int uploadPapers(List<Papers> papers);
}
