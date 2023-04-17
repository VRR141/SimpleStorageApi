package org.vrr.simplecloudservice.properties;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@Getter
@PropertySource("classpath:cache.properties")
public class CacheProperties {

    @Value("${logout.cache.duration}")
    private Integer logoutCacheDuration;

    @Value("${user.details.cache.duration}")
    private Integer userDetailsCacheDuration;
}
