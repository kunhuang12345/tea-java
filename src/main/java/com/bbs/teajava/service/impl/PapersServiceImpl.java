package com.bbs.teajava.service.impl;

import com.bbs.teajava.entity.Papers;
import com.bbs.teajava.mapper.PapersMapper;
import com.bbs.teajava.service.IPapersService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  论文实现类
 * </p>
 *
 * @author hk
 * @since 2024-11-25
 */
@Service
public class PapersServiceImpl extends ServiceImpl<PapersMapper, Papers> implements IPapersService {

    @Autowired
    private PapersMapper papersMapper;
    @Override
    public List<Papers> getAllPapers() {
        return papersMapper.getAllPapers();
    }

    @Override
    public int uploadPapers(List<Papers> papers) {
        // TODO 论文上传
        return 0;
    }
}
