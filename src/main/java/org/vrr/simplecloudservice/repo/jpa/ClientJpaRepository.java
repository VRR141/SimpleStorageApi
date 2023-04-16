package org.vrr.simplecloudservice.repo.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.vrr.simplecloudservice.domain.Client;

import java.util.Optional;
import java.util.UUID;

public interface ClientJpaRepository extends JpaRepository<Client, Long> {

    Optional<Client> findByUuid(UUID uuid);
}
