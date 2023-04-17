package org.vrr.simplecloudservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.vrr.simplecloudservice.domain.Client;
import org.vrr.simplecloudservice.domain.ClientProfile;
import org.vrr.simplecloudservice.repo.ClientProfileRepository;
import org.vrr.simplecloudservice.repo.jpa.ClientJpaRepository;
import org.vrr.simplecloudservice.service.CloudStorageService;
import org.vrr.simplecloudservice.service.RegistrationService;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class RegistrationServiceImpl implements RegistrationService {

    private final ClientProfileRepository clientProfileRepository;

    private final ClientJpaRepository clientJpaRepository;

    private final CloudStorageService cloudStorageService;

    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void registerClient(ClientProfile clientProfile) {
        UUID uuid = UUID.randomUUID();
        Client client = clientProfile.getClient();
        client.setUuid(uuid);

        String email = clientProfile.getEmail();
        clientProfileRepository.checkExistenceEmail(email);

        String password = clientProfile.getPassword();
        clientProfile.setPassword(passwordEncoder.encode(password));
        clientProfile.setClient(client);

        clientJpaRepository.save(client);
        clientProfileRepository.save(clientProfile);

        cloudStorageService.createBucket(String.valueOf(uuid));

        log.info("Client with email {} successfully registered", email);
    }
}
