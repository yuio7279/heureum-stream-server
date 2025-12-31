package org.heureum.api.session.api.dto;

import java.time.Instant;
import java.util.UUID;

public record StartSessionResponse(
        UUID sessionId,
        String streamId,
        String userId,
        Instant startedAt
) {
}
