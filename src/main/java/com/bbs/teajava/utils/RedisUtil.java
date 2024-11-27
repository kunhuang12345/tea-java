package com.bbs.teajava.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class RedisUtil {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private ObjectMapper objectMapper; // Jackson
    private final Logger logger = LoggerFactory.getLogger(RedisUtil.class);

    public void set(String key, String value) {
        stringRedisTemplate.opsForValue().set(key, value);
    }
    public String get(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }


    /**
     * 存储对象
     * @param key 键
     * @param value 对象
     * @param timeout 超时时间
     * @param unit 时间单位
     */
    public void setObject(String key, Object value, long timeout, TimeUnit unit) {
        try {
            String jsonValue = objectMapper.writeValueAsString(value);
            stringRedisTemplate.opsForValue().set(key, jsonValue, timeout, unit);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Redis序列化失败", e);
        }
    }

    /**
     * 存储对象
     * @param key 键
     * @param clazz 返回对象类型
     * @return 对象
     * @param <T> 对象类型
     */
    public <T> T getObject(String key, Class<T> clazz) {
        try {
            String jsonValue = stringRedisTemplate.opsForValue().get(key);
            if (StringUtils.isEmpty(jsonValue)) {
                return null;
            }
            return objectMapper.readValue(jsonValue, clazz);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Redis反序列化失败", e);
        }
    }

    /*----------------------------------------------------------------------*/
    // hash

    /**
     * 存储Hash结构数据并设置过期时间
     * @param key Redis键
     * @param field Hash字段
     * @param value 值
     * @param timeout 过期时间
     * @param unit 时间单位
     */
    public void hSet(String key, String field, String value, long timeout, TimeUnit unit) {
        try {
            stringRedisTemplate.opsForHash().put(key, field, value);
            stringRedisTemplate.expire(key, timeout, unit);
        } catch (Exception e) {
            logger.error("Redis hSet error: ", e);
            throw new RuntimeException("Redis操作失败");
        }
    }

    /**
     * 批量存储Hash结构数据并设置过期时间
     * @param key Redis键
     * @param map Hash字段和值的映射
     * @param timeout 过期时间
     * @param unit 时间单位
     */
    public void hSetAll(String key, Map<String, String> map, long timeout, TimeUnit unit) {
        try {
            stringRedisTemplate.opsForHash().putAll(key, map);
            stringRedisTemplate.expire(key, timeout, unit);
        } catch (Exception e) {
            logger.error("Redis hSetAll error: ", e);
            throw new RuntimeException("Redis操作失败");
        }
    }

    /**
     * 获取Hash中的单个字段值
     */
    public String hGet(String key, String field) {
        return (String) stringRedisTemplate.opsForHash().get(key, field);
    }

    /**
     * 获取Hash中的所有字段和值
     */
    public Map<Object, Object> hGetAll(String key) {
        return stringRedisTemplate.opsForHash().entries(key);
    }

    /**
     * 删除Hash中的字段
     */
    public void hDelete(String key, Object... fields) {
        stringRedisTemplate.opsForHash().delete(key, fields);
    }

    /**
     * 判断Hash中是否存在字段
     */
    public boolean hExists(String key, String field) {
        return stringRedisTemplate.opsForHash().hasKey(key, field);
    }

    /*-----------------------------------------------------------------------*/
    // List


    /**
     * 从左边添加元素(可以添加多个)
     */
    public void leftPush(String key, long timeout, TimeUnit timeUnit, String... values) {
        try {
            stringRedisTemplate.opsForList().leftPushAll(key, values);
            // 设置过期时间
            stringRedisTemplate.expire(key, timeout, timeUnit);
        } catch (Exception e) {
            logger.error("Redis leftPush error: ", e);
            throw new RuntimeException("Redis操作失败");
        }
    }

    /**
     * 从右边添加元素
     */
    public void rightPush(String key, long timeout, TimeUnit timeUnit, String... values) {
        try {
            stringRedisTemplate.opsForList().rightPushAll(key, values);
            stringRedisTemplate.expire(key, timeout, timeUnit);
        } catch (Exception e) {
            logger.error("Redis rightPush error: ", e);
            throw new RuntimeException("Redis操作失败");
        }
    }

    /**
     * 替换指定位置的值
     * @param key Redis键
     * @param index 索引位置
     * @param value 新值
     */
    public void setListByIndex(String key, long index, String value) {
        try {
            // index必须在list范围内，否则会报错
            stringRedisTemplate.opsForList().set(key, index, value);
            stringRedisTemplate.expire(key, 10, TimeUnit.MINUTES);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * 替换指定位置的值
     * @param key Redis键
     * @param index 索引位置
     * @param value 新值
     */
    public void setListByIndex(String key, long index, String value, long timeout, TimeUnit timeUnit) {
        try {
            // index必须在list范围内，否则会报错
            stringRedisTemplate.opsForList().set(key, index, value);
            stringRedisTemplate.expire(key, timeout, timeUnit);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * 获取指定范围的元素
     * start 和 end 可以是负数，-1表示最后一个元素
     */
    public List<String> range(String key, long start, long end) {
        return stringRedisTemplate.opsForList().range(key, start, end);
    }

    /**
     * 获取列表长度
     */
    public Long size(String key) {
        return stringRedisTemplate.opsForList().size(key);
    }

    /**
     * 从左边弹出元素
     */
    public String leftPop(String key) {
        return stringRedisTemplate.opsForList().leftPop(key);
    }

    /**
     * 从右边弹出元素
     */
    public String rightPop(String key) {
        return stringRedisTemplate.opsForList().rightPop(key);
    }

}
