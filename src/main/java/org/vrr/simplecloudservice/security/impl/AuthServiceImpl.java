package org.vrr.simplecloudservice.security.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;
import org.vrr.simplecloudservice.dto.request.AuthenticationRequestDto;
import org.vrr.simplecloudservice.security.AuthProvider;
import org.vrr.simplecloudservice.security.AuthService;
import org.vrr.simplecloudservice.security.jwt.JwtUtil;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;

    private final AuthProvider authProvider;

    private final JwtUtil jwtUtil;

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
}
