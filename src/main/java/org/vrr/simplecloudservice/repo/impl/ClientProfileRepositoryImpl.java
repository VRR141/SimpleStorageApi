package org.vrr.simplecloudservice.repo.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;
import org.vrr.simplecloudservice.domain.Client;
import org.vrr.simplecloudservice.domain.ClientProfile;
import org.vrr.simplecloudservice.excecption.ClientNotFoundByEmailException;
import org.vrr.simplecloudservice.excecption.ClientNotFoundByUuidException;
import org.vrr.simplecloudservice.excecption.EmailAlreadyExistException;
import org.vrr.simplecloudservice.repo.ClientProfileRepository;
import org.vrr.simplecloudservice.repo.jpa.ClientProfileJpaRepository;

import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class ClientProfileRepositoryImpl implements ClientProfileRepository {

    private final ClientProfileJpaRepository clientProfileJpaRepository;

    @Override
    public void checkExistenceEmail(String email) {
        boolean exist = clientProfileJpaRepository.existsByEmail(email);
        if (exist){
            throw new EmailAlreadyExistException(email);
        }
    }

    @Override
    public ClientProfile findByEmail(String email) {
        return clientProfileJpaRepository.findClientProfileByEmail(email)
                .orElseThrow(() -> new ClientNotFoundByEmailException(email));
    }

    @Override
    public void save(ClientProfile clientProfile) {
        clientProfileJpaRepository.save(clientProfile);
    }

    @Override
    public Client findClientByEmail(String email) {
        return clientProfileJpaRepository.findClientProfileAndFetchedClientByEmail(email)
                .orElseThrow(() -> new ClientNotFoundByEmailException(email))
                .getClient();
    }

    @Override
    @Cacheable(key = "#uuid", value = "userDetailsCache")
    public ClientProfile findByUuid(String uuid) {
        return clientProfileJpaRepository.findClientProfileByClientUuid(UUID.fromString(uuid))
                .orElseThrow(() -> new ClientNotFoundByUuidException(uuid));
    }
}
