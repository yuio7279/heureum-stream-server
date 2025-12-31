package org.heureum.core.repository.session;

import org.heureum.core.domain.session.StreamSession;
import org.heureum.core.domain.stream.StreamId;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface StreamSessionRepository {
    StreamSession save(StreamSession session);

    Optional<StreamSession> findById(UUID sessionId);

    List<StreamSession> findByStreamId(StreamId streamId);
}
