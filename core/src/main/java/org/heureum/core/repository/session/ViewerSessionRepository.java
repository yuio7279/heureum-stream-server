package org.heureum.core.repository.session;

import org.heureum.core.domain.session.ViewerSession;
import org.heureum.core.domain.stream.StreamId;
import org.heureum.core.domain.user.ViewerId;

import java.time.Duration;
import java.util.Optional;

public interface ViewerSessionRepository {
    ViewerSession save(ViewerSession session, Duration ttl);

    Optional<ViewerSession> findByStreamIdAndViewerId(StreamId streamId, ViewerId viewerId);

    void delete(StreamId streamId, ViewerId viewerId);
}
