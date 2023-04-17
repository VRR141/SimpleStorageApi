package org.vrr.simplecloudservice.configuration;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.vrr.simplecloudservice.properties.CacheProperties;

import java.util.concurrent.TimeUnit;

@Configuration
@RequiredArgsConstructor
public class CacheConfiguration {

    private final CacheProperties cacheProperties;

    @Bean("logoutCache")
    Cache<String, Object> logoutCache(){
        return Caffeine.newBuilder()
                .expireAfterWrite(cacheProperties.getLogoutCacheDuration(), TimeUnit.MINUTES)
                .build();
    }

    @Bean("userDetailsCache")
    Caffeine userDetailsCache() {
        return Caffeine.newBuilder()
                .expireAfterWrite(cacheProperties.getUserDetailsCacheDuration(), TimeUnit.MINUTES);
    }
    @Bean
    CacheManager cacheManager(@Qualifier("userDetailsCache") Caffeine caffeine){
        CaffeineCacheManager manager = new CaffeineCacheManager();
        manager.setCaffeine(caffeine);
        return manager;
    }
}
