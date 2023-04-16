package org.vrr.simplecloudservice.repo.jpa;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.vrr.simplecloudservice.domain.ClientProfile;

import java.util.Optional;
import java.util.UUID;

public interface ClientProfileJpaRepository extends CrudRepository<ClientProfile, Long> {

    boolean existsByEmail(String email);

    Optional<ClientProfile> findClientProfileByEmail(String email);

    @Query("from ClientProfile cp join fetch cp.client where cp.email = :email")
    Optional<ClientProfile> findClientProfileAndFetchedClientByEmail(String email);

    Optional<ClientProfile> findClientProfileByClientUuid(UUID uuid);
}
