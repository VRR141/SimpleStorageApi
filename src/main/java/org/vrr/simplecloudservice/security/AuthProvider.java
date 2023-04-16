package org.vrr.simplecloudservice.security;

import org.vrr.simplecloudservice.domain.Client;
import org.vrr.simplecloudservice.dto.request.AuthenticationRequestDto;

import java.util.UUID;

public interface AuthProvider {

    UUID getAuthorizedUserUuid();

    Client getAuthorizedUser();

    String getLoginFromAuthRequestDto(AuthenticationRequestDto dto);
}
