package org.heureum.core.domain.session;

import lombok.Getter;
import org.heureum.core.domain.stream.StreamId;
import org.heureum.core.domain.user.UserId;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Getter
public final class StreamSession {
    private final UUID sessionId;
    private final StreamId streamId;
    private final UserId userId;
    private final Instant startedAt;
    private Instant lastHeartbeatAt;
    private StreamSessionStatus status;

    public StreamSession(UUID sessionId, StreamId streamId, UserId userId, Instant startedAt) {
        this(sessionId, streamId, userId, startedAt, startedAt, StreamSessionStatus.ACTIVE);
    }

    private StreamSession(UUID sessionId,
                          StreamId streamId,
                          UserId userId,
                          Instant startedAt,
                          Instant lastHeartbeatAt,
                          StreamSessionStatus status) {
        this.sessionId = Objects.requireNonNull(sessionId, "sessionId");
        this.streamId = Objects.requireNonNull(streamId, "streamId");
        this.userId = Objects.requireNonNull(userId, "userId");
        this.startedAt = Objects.requireNonNull(startedAt, "startedAt");
        this.lastHeartbeatAt = Objects.requireNonNull(lastHeartbeatAt, "lastHeartbeatAt");
        this.status = Objects.requireNonNull(status, "status");
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

    public static StreamSession restore(UUID sessionId,
                                        StreamId streamId,
                                        UserId userId,
                                        Instant startedAt,
                                        Instant lastHeartbeatAt,
                                        StreamSessionStatus status) {
        return new StreamSession(sessionId, streamId, userId, startedAt, lastHeartbeatAt, status);
    }
}
