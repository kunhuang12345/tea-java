package com.bbs.teajava.service;

import com.bbs.teajava.entity.Papers;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bbs.teajava.utils.ApiResultUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
     * @return 所有论文列表
     */
    List<Papers> getAllPapers();

    /**
     * 上传论文
     * @param papers 论文列表信息
     * @return 上传结果
     */
    int uploadPapers(List<Papers> papers);

    /**
     * 上传文件 测试
     * @param file 文件
     * @return 存储结果
     */
    ApiResultUtils uploadFile(MultipartFile file);
}
