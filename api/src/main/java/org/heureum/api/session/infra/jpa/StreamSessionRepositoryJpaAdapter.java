package org.heureum.api.session.infra.jpa;

import lombok.RequiredArgsConstructor;
import org.heureum.core.domain.session.StreamSession;
import org.heureum.core.domain.stream.StreamId;
import org.heureum.core.domain.user.UserId;
import org.heureum.core.repository.session.StreamSessionRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class StreamSessionRepositoryJpaAdapter implements StreamSessionRepository {
    private final StreamSessionJpaRepository jpaRepository;

    @Override
    public StreamSession save(StreamSession session) {
        StreamSessionJpaEntity entity = toEntity(session);
        StreamSessionJpaEntity saved = jpaRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<StreamSession> findById(UUID sessionId) {
        return jpaRepository.findById(sessionId).map(this::toDomain);
    }

    @Override
    public List<StreamSession> findByStreamId(StreamId streamId) {
        return jpaRepository.findByStreamId(streamId.value()).stream().map(this::toDomain).toList();
    }

    private StreamSessionJpaEntity toEntity(StreamSession session) {
        return new StreamSessionJpaEntity(
                session.getSessionId(),
                session.getStreamId().value(),
                session.getUserId().value(),
                session.getStartedAt(),
                session.getLastHeartbeatAt(),
                session.getStatus()
        );
    }

    private StreamSession toDomain(StreamSessionJpaEntity entity) {
        return StreamSession.restore(
                entity.getSessionId(),
                StreamId.of(entity.getStreamId()),
                UserId.of(entity.getUserId()),
                entity.getStartedAt(),
                entity.getLastHeartbeatAt(),
                entity.getStatus()
        );
    }
}
