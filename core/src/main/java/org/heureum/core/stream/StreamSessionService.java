package org.heureum.core.stream;

import org.heureum.common.time.TimeProvider;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class StreamSessionService {
    private final StreamSessionRepository repository;
    private final TimeProvider timeProvider;

    public StreamSessionService(StreamSessionRepository repository, TimeProvider timeProvider) {
        this.repository = Objects.requireNonNull(repository, "repository");
        this.timeProvider = Objects.requireNonNull(timeProvider, "timeProvider");
    }

    public StreamSession startSession(String streamId, String userId) {
        validateId(streamId, "streamId");
        validateId(userId, "userId");
        Instant now = timeProvider.now();
        StreamSession session = new StreamSession(UUID.randomUUID(), streamId, userId, now);
        return repository.save(session);
    }

    public StreamSession heartbeat(UUID sessionId) {
        StreamSession session = getSessionOrThrow(sessionId);
        session.heartbeat(timeProvider.now());
        return repository.save(session);
    }

    public StreamSession endSession(UUID sessionId) {
        StreamSession session = getSessionOrThrow(sessionId);
        session.end(timeProvider.now());
        return repository.save(session);
    }

    public int countActiveSessions(String streamId) {
        validateId(streamId, "streamId");
        List<StreamSession> sessions = repository.findByStreamId(streamId);
        int activeCount = 0;
        for (StreamSession session : sessions) {
            if (session.getStatus() == StreamSessionStatus.ACTIVE) {
                activeCount += 1;
            }
        }
        return activeCount;
    }

    private StreamSession getSessionOrThrow(UUID sessionId) {
        Objects.requireNonNull(sessionId, "sessionId");
        return repository.findById(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("Session not found: " + sessionId));
    }

    private void validateId(String value, String name) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(name + " must not be blank.");
        }
    }
}
