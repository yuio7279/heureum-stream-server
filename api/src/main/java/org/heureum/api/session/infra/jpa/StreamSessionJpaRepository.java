package org.heureum.api.session.infra.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface StreamSessionJpaRepository extends JpaRepository<StreamSessionJpaEntity, UUID> {
    List<StreamSessionJpaEntity> findByStreamId(String streamId);
}
