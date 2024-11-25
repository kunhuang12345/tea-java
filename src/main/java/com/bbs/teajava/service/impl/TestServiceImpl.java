package com.bbs.teajava.service.impl;

import com.bbs.teajava.service.TestService;
import com.bbs.teajava.utils.RedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class TestServiceImpl implements TestService {
    Logger logger = LoggerFactory.getLogger(TestServiceImpl.class);
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired


    @Override
    public void test() {
        redisUtil.get("test");
        redisUtil.set("test", "test");
        logger.info(redisUtil.get("test"));
    }
}