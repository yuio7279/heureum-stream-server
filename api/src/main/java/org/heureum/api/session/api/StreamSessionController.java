package org.heureum.api.session.api;

import jakarta.validation.Valid;
import org.heureum.api.session.api.dto.ActiveSessionsResponse;
import org.heureum.api.session.api.dto.SessionStatusResponse;
import org.heureum.api.session.api.dto.StartSessionRequest;
import org.heureum.api.session.api.dto.StartSessionResponse;
import org.heureum.core.domain.session.StreamSession;
import org.heureum.core.service.session.StreamSessionService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/streams")
public class StreamSessionController {
    private final StreamSessionService sessionService;

    public StreamSessionController(StreamSessionService sessionService) {
        this.sessionService = sessionService;
    }

    @PostMapping("/{streamId}/sessions")
    @ResponseStatus(HttpStatus.CREATED)
    public StartSessionResponse startSession(
            @PathVariable String streamId,
            @Valid @RequestBody StartSessionRequest request
    ) {
        StreamSession session = sessionService.startSession(streamId, request.userId());
        return new StartSessionResponse(
                session.getSessionId(),
                session.getStreamId().value(),
                session.getUserId().value(),
                session.getStartedAt()
        );
    }

    @PostMapping("/sessions/{sessionId}/heartbeat")
    public SessionStatusResponse heartbeat(@PathVariable UUID sessionId) {
        StreamSession session = sessionService.heartbeat(sessionId);
        return new SessionStatusResponse(
                session.getSessionId(),
                session.getStatus(),
                session.getLastHeartbeatAt()
        );
    }

    @DeleteMapping("/sessions/{sessionId}")
    public SessionStatusResponse endSession(@PathVariable UUID sessionId) {
        StreamSession session = sessionService.endSession(sessionId);
        return new SessionStatusResponse(
                session.getSessionId(),
                session.getStatus(),
                session.getLastHeartbeatAt()
        );
    }

    @GetMapping("/{streamId}/sessions/active")
    public ActiveSessionsResponse activeSessions(@PathVariable String streamId) {
        int activeSessions = sessionService.countActiveSessions(streamId);
        return new ActiveSessionsResponse(streamId, activeSessions);
    }
}
