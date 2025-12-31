package org.heureum.core.service.session;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.heureum.common.time.TimeProvider;
import org.heureum.core.domain.session.StreamSession;
import org.heureum.core.domain.session.StreamSessionStatus;
import org.heureum.core.domain.stream.StreamId;
import org.heureum.core.domain.user.UserId;
import org.heureum.core.repository.session.StreamSessionRepository;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@RequiredArgsConstructor
public class StreamSessionService {
    private final @NonNull StreamSessionRepository repository;
    private final @NonNull TimeProvider timeProvider;

    public StreamSession startSession(String streamId, String userId) {
        StreamId stream = StreamId.of(requireId(streamId, "streamId"));
        UserId user = UserId.of(requireId(userId, "userId"));
        Instant now = timeProvider.now();
        StreamSession session = new StreamSession(UUID.randomUUID(), stream, user, now);
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
        StreamId stream = StreamId.of(requireId(streamId, "streamId"));
        List<StreamSession> sessions = repository.findByStreamId(stream);
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

    private String requireId(String value, String name) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(name + " must not be blank.");
        }
        return value;
    }
}
