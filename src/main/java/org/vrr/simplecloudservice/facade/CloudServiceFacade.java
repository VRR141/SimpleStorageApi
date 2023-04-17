package org.vrr.simplecloudservice.facade;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.vrr.simplecloudservice.domain.ClientProfile;
import org.vrr.simplecloudservice.dto.request.RegistrationRequestDto;
import org.vrr.simplecloudservice.mapper.ClientProfileMapper;
import org.vrr.simplecloudservice.security.AuthProvider;
import org.vrr.simplecloudservice.service.RegistrationService;

@Component
@RequiredArgsConstructor
@Slf4j
public class CloudServiceFacade {

    private final RegistrationService registrationService;

    private final ClientProfileMapper mapper;

    public void registerClient(RegistrationRequestDto dto){
        ClientProfile clientProfile = mapper.mapRegistrationRequestDtoToClientProfile(dto);
        registrationService.registerClient(clientProfile);
    }
}
