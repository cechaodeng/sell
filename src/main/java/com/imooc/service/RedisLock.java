package com.imooc.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.test.annotation.Commit;
import org.springframework.test.annotation.SystemProfileValueSource;
import org.springframework.util.StringUtils;

/**
 * @author Kent
 * @date 2017-11-20.
 */
@Component
@Slf4j
public class RedisLock {
    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 加锁
     * @param key
     * @param value
     * @return
     */
    public boolean lock(String key, String value) {
        if (redisTemplate.opsForValue().setIfAbsent(key, value)) {
            //如果可以设置,说明当前key没有被其他现成占用
            return true;
        }

        //必须要设置超时时间,不然可能会造成死锁现象

        //不能设置,说明当前key被其他现成占用,则判断占用key的现成是否占用时间过长,已经过期
        String currentvalue = redisTemplate.opsForValue().get(key);//这里是上一个锁的时间
        if (!StringUtils.isEmpty(currentvalue) && Long.parseLong(currentvalue) < System.currentTimeMillis()) {
            //如果value(set时间+超时时间)<当前系统时间,说明之前的占用key的线程超时了
            String oldValue = redisTemplate.opsForValue().getAndSet(key, value);//这行代码在同一个时间只能一个线程执行
            if (!StringUtils.isEmpty(oldValue) && oldValue.equals(currentvalue)) {
                //如果getAndSet得到的是上一个锁的时间,说明上一个之后,他是第一个拿到锁的
                return true;
            }
        }
        return false;
    }

    public void unlock(String key, String value) {
        try {
            String currentValue = redisTemplate.opsForValue().get(key);
            //key-value都相同,表示同一个redis
            if (!StringUtils.isEmpty(currentValue) && currentValue.equals(value)) {
                redisTemplate.opsForValue().getOperations().delete(key);

            }
        } catch (Exception e) {
            log.error("[redis分布式锁]解锁异常,{}", e);
        }
    }
}
