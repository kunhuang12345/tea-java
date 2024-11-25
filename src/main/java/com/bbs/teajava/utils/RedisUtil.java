package com.bbs.teajava.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisUtil {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    public void set(String key, String value) {
        stringRedisTemplate.opsForValue().set(key, value);
    }
    public String get(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }
}
