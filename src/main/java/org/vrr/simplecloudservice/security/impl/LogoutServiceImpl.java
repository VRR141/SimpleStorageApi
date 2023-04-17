package org.vrr.simplecloudservice.security.impl;

import com.github.benmanes.caffeine.cache.Cache;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.vrr.simplecloudservice.security.LogoutService;
import org.vrr.simplecloudservice.security.jwt.JwtUtil;

@Component
@RequiredArgsConstructor
@Slf4j
public class LogoutServiceImpl implements LogoutService {

    private final Cache<String, Object> logoutCache;

    private final JwtUtil jwtUtil;

    private static final Object mock = new Object();

    @Override
    public void logout(String jwt) {
        logoutCache.put(jwt, mock);
        log.info("Logout successfully registered");
    }

    @Override
    public boolean checkLogoutExistence(String jwt) {
        var s = logoutCache.getIfPresent(jwt);
        if (s == null){
            return false;
        } else {
            return s == mock;
        }
    }
}
