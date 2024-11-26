package com.bbs.teajava.service.impl;

import com.bbs.teajava.entity.Comments;
import com.bbs.teajava.mapper.CommentsMapper;
import com.bbs.teajava.service.ICommentsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  评论实现类
 * </p>
 *
 * @author hk
 * @since 2024-11-25
 */
@Service
public class CommentsServiceImpl extends ServiceImpl<CommentsMapper, Comments> implements ICommentsService {

}
