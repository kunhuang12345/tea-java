package com.bbs.teajava.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bbs.teajava.entity.PaperAuthor;
import com.bbs.teajava.entity.Users;
import com.bbs.teajava.mapper.PaperAuthorMapper;
import com.bbs.teajava.service.IPaperAuthorService;
import com.bbs.teajava.service.IUsersService;
import com.bbs.teajava.utils.ApiResultUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.aop.framework.AopContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author kunhuang
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PaperAuthorImpl extends ServiceImpl<PaperAuthorMapper, PaperAuthor> implements IPaperAuthorService {

    private final IUsersService userService;
    private final PaperAuthorMapper paperAuthorMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResultUtils add(Integer paperId, String userIds) {
        try {
            List<Integer> userIdList = Arrays.stream(userIds.split(",")).filter(StringUtils::isNotBlank).map(Integer::parseInt).toList();
            List<PaperAuthor> paperAuthorList = new ArrayList<>();
            for (Integer userId : userIdList) {
                PaperAuthor paperAuthor = new PaperAuthor();
                paperAuthor.setPaperId(paperId);
                paperAuthor.setUserId(userId);
                paperAuthorList.add(paperAuthor);
            }
            IPaperAuthorService proxy = (PaperAuthorImpl)AopContext.currentProxy();
            proxy.saveBatch(paperAuthorList);
            return ApiResultUtils.success(userIdList);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ApiResultUtils.error(500, "请确保用户存在");
        }
    }

    @Override
    public ApiResultUtils getAuthorList(Integer paperId) {
        List<PaperAuthor> authorList = this.list(new QueryWrapper<PaperAuthor>().eq("paper_id", paperId));
        List<Integer> list = authorList.stream().map(PaperAuthor::getUserId).toList();
        List<Users> userList = userService.list(new QueryWrapper<Users>().in("id", list));
        return ApiResultUtils.success(userList);
    }
}