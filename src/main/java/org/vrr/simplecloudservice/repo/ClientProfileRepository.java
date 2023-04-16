package org.vrr.simplecloudservice.repo;

import org.vrr.simplecloudservice.domain.Client;
import org.vrr.simplecloudservice.domain.ClientProfile;

public interface ClientProfileRepository {

    void checkExistenceEmail(String email);

    ClientProfile findByEmail(String email);

    void save(ClientProfile clientProfile);

    Client findClientByEmail(String email);

    ClientProfile findByUuid(String uuid);
}
