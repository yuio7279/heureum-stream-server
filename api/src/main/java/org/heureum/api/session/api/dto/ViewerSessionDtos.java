package org.heureum.api.session.api.dto;

import jakarta.validation.constraints.NotBlank;

import java.time.Instant;

public final class ViewerSessionDtos {
    private ViewerSessionDtos() {
    }

    public record StartViewerSessionRequest(@NotBlank String viewerId) {
    }

    public record ViewerSessionResponse(String streamId,
                                        String viewerId,
                                        Instant connectedAt,
                                        Instant lastHeartbeatAt) {
    }
}
