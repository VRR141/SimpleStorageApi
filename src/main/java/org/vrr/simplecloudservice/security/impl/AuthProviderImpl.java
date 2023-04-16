package org.vrr.simplecloudservice.security.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.vrr.simplecloudservice.domain.Client;
import org.vrr.simplecloudservice.dto.request.AuthenticationRequestDto;
import org.vrr.simplecloudservice.repo.ClientProfileRepository;
import org.vrr.simplecloudservice.repo.jpa.ClientJpaRepository;
import org.vrr.simplecloudservice.security.AuthProvider;

import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthProviderImpl implements AuthProvider {

    private final ClientJpaRepository clientJpaRepository;

    private final ClientProfileRepository clientProfileRepository;

    @Override
    public UUID getAuthorizedUserUuid() {
        return UUID.fromString(getUserDetails().getUsername());
    }

    @Override
    public Client getAuthorizedUser() {
        return clientJpaRepository.findByUuid(UUID.fromString(
                getUserDetails().getUsername()
        )).orElseThrow();
    }

    @Override
    public String getLoginFromAuthRequestDto(AuthenticationRequestDto dto) {
        return clientProfileRepository.findClientByEmail(dto.getEmail()).getUuid().toString();
    }


    private UserDetails getUserDetails(){
        return (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
    }

}
