package org.heureum.api.stream.api.dto;

import java.time.Instant;

public class StreamDtos {

    public record CreateStreamRequest(String title) {}

    public record CreateStreamResponse(
            String streamId,
            String streamKey,
            String status,
            Instant createdAt
    ) {}

    public record IssueWatchTokenResponse(
            String token,
            Instant expiresAt
    ) {}
}
