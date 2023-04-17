package org.vrr.simplecloudservice.configuration;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class LogoutCacheConfiguration {

    private Integer cacheDuration = 15;

    @Bean("logoutCache")
    Cache<String, String> logoutCache(){
        return Caffeine.newBuilder()
                .expireAfterWrite(cacheDuration, TimeUnit.MINUTES)
                .build();
    }
}
