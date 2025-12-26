package org.heureum.api.stream.infra.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StreamJpaRepository extends JpaRepository<StreamJpaEntity, String> {
    Optional<StreamJpaEntity> findByStreamKey(String streamKey);
}


