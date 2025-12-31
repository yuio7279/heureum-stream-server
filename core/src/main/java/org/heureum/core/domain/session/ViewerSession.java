package org.heureum.core.domain.session;

import lombok.Getter;
import org.heureum.core.domain.stream.StreamId;
import org.heureum.core.domain.user.ViewerId;

import java.time.Instant;
import java.util.Objects;

@Getter
public final class ViewerSession {
    private final StreamId streamId;
    private final ViewerId viewerId;
    private final Instant connectedAt;
    private Instant lastHeartbeatAt;

    public ViewerSession(StreamId streamId, ViewerId viewerId, Instant connectedAt) {
        this(streamId, viewerId, connectedAt, connectedAt);
    }

    private ViewerSession(StreamId streamId,
                          ViewerId viewerId,
                          Instant connectedAt,
                          Instant lastHeartbeatAt) {
        this.streamId = Objects.requireNonNull(streamId, "streamId");
        this.viewerId = Objects.requireNonNull(viewerId, "viewerId");
        this.connectedAt = Objects.requireNonNull(connectedAt, "connectedAt");
        this.lastHeartbeatAt = Objects.requireNonNull(lastHeartbeatAt, "lastHeartbeatAt");
    }

    public void heartbeat(Instant heartbeatAt) {
        this.lastHeartbeatAt = Objects.requireNonNull(heartbeatAt, "heartbeatAt");
    }

    public static ViewerSession restore(StreamId streamId,
                                        ViewerId viewerId,
                                        Instant connectedAt,
                                        Instant lastHeartbeatAt) {
        return new ViewerSession(streamId, viewerId, connectedAt, lastHeartbeatAt);
    }
}
