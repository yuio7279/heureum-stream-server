package org.heureum.api.stream;

import java.time.Instant;
import java.util.UUID;

public record StartSessionResponse(
        UUID sessionId,
        String streamId,
        String userId,
        Instant startedAt
) {
}
