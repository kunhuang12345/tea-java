package com.bbs.teajava.service.impl;

import com.bbs.teajava.entity.Papers;
import com.bbs.teajava.mapper.PapersMapper;
import com.bbs.teajava.service.IPapersService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bbs.teajava.utils.ApiResultUtils;
import com.bbs.teajava.utils.MinioUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * <p>
 * 论文实现类
 * </p>
 *
 * @author hk
 * @since 2024-11-25
 */
@Service
public class PapersServiceImpl extends ServiceImpl<PapersMapper, Papers> implements IPapersService {

    @Autowired
    private PapersMapper papersMapper;

    @Autowired
    private MinioUtil minioUtil;

    @Override
    public List<Papers> getAllPapers() {
        return papersMapper.getAllPapers();
    }

    @Override
    public int uploadPapers(List<Papers> papers) {
        // TODO 论文上传
        return 0;
    }

    @Override
    public ApiResultUtils uploadFile(MultipartFile file) {
        try {
            minioUtil.uploadFile("temp", file.getOriginalFilename(), file.getInputStream());
            return ApiResultUtils.success("上传成功");
        } catch (IOException e) {
            return ApiResultUtils.error(500, "上传失败");
        }
    }
}
