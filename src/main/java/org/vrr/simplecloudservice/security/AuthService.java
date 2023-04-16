package org.vrr.simplecloudservice.security;

import org.vrr.simplecloudservice.dto.request.AuthenticationRequestDto;
import org.vrr.simplecloudservice.dto.response.AuthenticationResponseDto;

public interface AuthService {

    String login(AuthenticationRequestDto dto);

}
