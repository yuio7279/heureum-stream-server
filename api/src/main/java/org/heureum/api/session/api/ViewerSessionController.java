package org.heureum.api.session.api;

import jakarta.validation.Valid;
import org.heureum.api.session.api.dto.ViewerSessionDtos.StartViewerSessionRequest;
import org.heureum.api.session.api.dto.ViewerSessionDtos.ViewerSessionResponse;
import org.heureum.core.domain.session.ViewerSession;
import org.heureum.core.service.session.ViewerSessionService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;

@RestController
@RequestMapping("/api/streams")
public class ViewerSessionController {
    private final ViewerSessionService viewerSessionService;

    public ViewerSessionController(ViewerSessionService viewerSessionService) {
        this.viewerSessionService = viewerSessionService;
    }

    @PostMapping("/{streamId}/viewers/sessions")
    @ResponseStatus(HttpStatus.CREATED)
    public ViewerSessionResponse startSession(
            @PathVariable String streamId,
            @Valid @RequestBody StartViewerSessionRequest request,
            @RequestParam(defaultValue = "60") long ttlSeconds
    ) {
        ViewerSession session = viewerSessionService.startSession(
                streamId,
                request.viewerId(),
                Duration.ofSeconds(ttlSeconds)
        );
        return new ViewerSessionResponse(
                session.getStreamId().value(),
                session.getViewerId().value(),
                session.getConnectedAt(),
                session.getLastHeartbeatAt()
        );
    }

    @PostMapping("/{streamId}/viewers/{viewerId}/heartbeat")
    public ViewerSessionResponse heartbeat(
            @PathVariable String streamId,
            @PathVariable String viewerId,
            @RequestParam(defaultValue = "60") long ttlSeconds
    ) {
        ViewerSession session = viewerSessionService.heartbeat(
                streamId,
                viewerId,
                Duration.ofSeconds(ttlSeconds)
        );
        return new ViewerSessionResponse(
                session.getStreamId().value(),
                session.getViewerId().value(),
                session.getConnectedAt(),
                session.getLastHeartbeatAt()
        );
    }

    @DeleteMapping("/{streamId}/viewers/{viewerId}/sessions")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void endSession(
            @PathVariable String streamId,
            @PathVariable String viewerId
    ) {
        viewerSessionService.endSession(streamId, viewerId);
    }
}
