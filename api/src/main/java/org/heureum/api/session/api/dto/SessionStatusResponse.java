package org.heureum.api.session.api.dto;

import org.heureum.core.domain.session.StreamSessionStatus;

import java.time.Instant;
import java.util.UUID;

public record SessionStatusResponse(
        UUID sessionId,
        StreamSessionStatus status,
        Instant lastHeartbeatAt
) {
}
