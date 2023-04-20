package com.libii.sso.web.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CacheConfigration {

    /**
     * @description  白名单缓存
     * @author fengchenxin
     * @date 2023/2/2 14:20
     */
    @Bean(name = "whiteListCache")
    public Cache<String, Integer> whiteListCache() {
        return Caffeine.newBuilder().build();
    }
}