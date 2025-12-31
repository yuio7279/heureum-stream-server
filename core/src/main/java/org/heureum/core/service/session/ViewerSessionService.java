package org.heureum.core.service.session;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.heureum.common.time.TimeProvider;
import org.heureum.core.domain.session.ViewerSession;
import org.heureum.core.domain.stream.StreamId;
import org.heureum.core.domain.user.ViewerId;
import org.heureum.core.repository.session.ViewerSessionRepository;

import java.time.Duration;
import java.util.Objects;

@RequiredArgsConstructor
public class ViewerSessionService {
    private final @NonNull ViewerSessionRepository repository;
    private final @NonNull TimeProvider timeProvider;

    public ViewerSession startSession(String streamId, String viewerId, Duration ttl) {
        StreamId stream = StreamId.of(requireId(streamId, "streamId"));
        ViewerId viewer = ViewerId.of(requireId(viewerId, "viewerId"));
        Objects.requireNonNull(ttl, "ttl");
        ViewerSession session = new ViewerSession(stream, viewer, timeProvider.now());
        return repository.save(session, ttl);
    }

    public ViewerSession heartbeat(String streamId, String viewerId, Duration ttl) {
        StreamId stream = StreamId.of(requireId(streamId, "streamId"));
        ViewerId viewer = ViewerId.of(requireId(viewerId, "viewerId"));
        Objects.requireNonNull(ttl, "ttl");
        ViewerSession session = repository.findByStreamIdAndViewerId(stream, viewer)
                .orElseThrow(() -> new IllegalArgumentException("Viewer session not found."));
        session.heartbeat(timeProvider.now());
        return repository.save(session, ttl);
    }

    public void endSession(String streamId, String viewerId) {
        StreamId stream = StreamId.of(requireId(streamId, "streamId"));
        ViewerId viewer = ViewerId.of(requireId(viewerId, "viewerId"));
        repository.delete(stream, viewer);
    }

    private String requireId(String value, String name) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(name + " must not be blank.");
        }
        return value;
    }
}
