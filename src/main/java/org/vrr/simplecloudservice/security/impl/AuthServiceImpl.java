package org.vrr.simplecloudservice.security.impl;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;
import org.vrr.simplecloudservice.dto.request.AuthenticationRequestDto;
import org.vrr.simplecloudservice.properties.JwtProperties;
import org.vrr.simplecloudservice.security.AuthProvider;
import org.vrr.simplecloudservice.security.AuthService;
import org.vrr.simplecloudservice.security.LogoutService;
import org.vrr.simplecloudservice.security.jwt.JwtUtil;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;

    private final AuthProvider authProvider;

    private final JwtUtil jwtUtil;

    private final LogoutService logoutService;

    private final JwtProperties jwtProperties;

    @Override
    public String login(AuthenticationRequestDto dto) {
        String uuid = authProvider.getLoginFromAuthRequestDto(dto);

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                uuid, dto.getPassword())
        );

        String token = jwtUtil.generateToken(uuid);

        log.info("Access token generated for user {}", uuid);
        return token;
    }

    @Override
    public void logout(HttpServletRequest request) {
        String jwt = request.getHeader(jwtProperties.getAuthorizationHeader());
        UUID authorizedUserUuid = authProvider.getAuthorizedUserUuid();
        log.info("Init logout for {}", authorizedUserUuid);
        logoutService.logout(jwt);
    }
}
