package org.heureum.api.stream;

import org.heureum.core.stream.StreamSessionStatus;

import java.time.Instant;
import java.util.UUID;

public record SessionStatusResponse(
        UUID sessionId,
        StreamSessionStatus status,
        Instant lastHeartbeatAt
) {
}
