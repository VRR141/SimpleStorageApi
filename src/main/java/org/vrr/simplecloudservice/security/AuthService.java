package org.vrr.simplecloudservice.security;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpRequest;
import org.vrr.simplecloudservice.dto.request.AuthenticationRequestDto;
import org.vrr.simplecloudservice.dto.response.AuthenticationResponseDto;

public interface AuthService {

    String login(AuthenticationRequestDto dto);

    void logout(HttpServletRequest request);
}
