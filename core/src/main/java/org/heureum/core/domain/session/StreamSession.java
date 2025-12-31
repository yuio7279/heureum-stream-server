package org.heureum.core.domain.session;

import lombok.Getter;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Getter
public final class StreamSession {
    private final UUID sessionId;
    private final String streamId;
    private final String userId;
    private final Instant startedAt;
    private Instant lastHeartbeatAt;
    private StreamSessionStatus status;

    public StreamSession(UUID sessionId, String streamId, String userId, Instant startedAt) {
        this.sessionId = Objects.requireNonNull(sessionId, "sessionId");
        this.streamId = Objects.requireNonNull(streamId, "streamId");
        this.userId = Objects.requireNonNull(userId, "userId");
        this.startedAt = Objects.requireNonNull(startedAt, "startedAt");
        this.lastHeartbeatAt = startedAt;
        this.status = StreamSessionStatus.ACTIVE;
    }

    public void heartbeat(Instant heartbeatAt) {
        if (status == StreamSessionStatus.ENDED) {
            throw new IllegalStateException("Session already ended.");
        }
        this.lastHeartbeatAt = Objects.requireNonNull(heartbeatAt, "heartbeatAt");
    }

    public void end(Instant endedAt) {
        Objects.requireNonNull(endedAt, "endedAt");
        if (status == StreamSessionStatus.ENDED) {
            return;
        }
        this.lastHeartbeatAt = endedAt;
        this.status = StreamSessionStatus.ENDED;
    }
}
