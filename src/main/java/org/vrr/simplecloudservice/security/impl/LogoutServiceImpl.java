package org.vrr.simplecloudservice.security.impl;

import com.github.benmanes.caffeine.cache.Cache;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;
import org.vrr.simplecloudservice.security.LogoutService;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
@Slf4j
public class LogoutServiceImpl implements LogoutService {

    private final Cache<String, String> logoutCache;

    @Override
    public void logout(String username, String jwt) {
        logoutCache.put(username, jwt);
        log.info("Logout for {} successfully registered", username);
    }

    @Override
    public boolean checkLogoutExistence(String username, String jwt) {
        String s = logoutCache.getIfPresent(username);
        if (s == null){
            return false;
        } else {
            return jwt.equals(s);
        }
    }
}
