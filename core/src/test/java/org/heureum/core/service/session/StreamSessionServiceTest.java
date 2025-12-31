package org.heureum.core.service.session;

import org.heureum.common.time.TimeProvider;
import org.heureum.core.domain.session.StreamSession;
import org.heureum.core.domain.session.StreamSessionStatus;
import org.heureum.core.domain.stream.StreamId;
import org.heureum.core.repository.session.StreamSessionRepository;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class StreamSessionServiceTest {

    @Test
    void startSessionCreatesActiveSession() {
        TimeProvider timeProvider = () -> Instant.parse("2024-01-01T00:00:00Z");
        StreamSessionService service = new StreamSessionService(
                new InMemoryStreamSessionRepository(),
                timeProvider
        );

        StreamSession session = service.startSession("stream-1", "user-1");

        assertEquals(StreamSessionStatus.ACTIVE, session.getStatus());
        assertEquals(Instant.parse("2024-01-01T00:00:00Z"), session.getLastHeartbeatAt());
    }

    @Test
    void heartbeatUpdatesTimestamp() {
        InMemoryStreamSessionRepository repository = new InMemoryStreamSessionRepository();
        TimeProvider timeProvider = () -> Instant.parse("2024-01-01T00:00:10Z");
        StreamSessionService service = new StreamSessionService(repository, timeProvider);

        StreamSession session = service.startSession("stream-1", "user-1");

        TimeProvider later = () -> Instant.parse("2024-01-01T00:00:20Z");
        StreamSessionService laterService = new StreamSessionService(repository, later);
        StreamSession updated = laterService.heartbeat(session.getSessionId());

        assertEquals(Instant.parse("2024-01-01T00:00:20Z"), updated.getLastHeartbeatAt());
    }

    @Test
    void endSessionMarksEnded() {
        InMemoryStreamSessionRepository repository = new InMemoryStreamSessionRepository();
        TimeProvider timeProvider = () -> Instant.parse("2024-01-01T00:00:10Z");
        StreamSessionService service = new StreamSessionService(repository, timeProvider);

        StreamSession session = service.startSession("stream-1", "user-1");

        TimeProvider later = () -> Instant.parse("2024-01-01T00:00:30Z");
        StreamSessionService laterService = new StreamSessionService(repository, later);
        StreamSession ended = laterService.endSession(session.getSessionId());

        assertEquals(StreamSessionStatus.ENDED, ended.getStatus());
        assertEquals(Instant.parse("2024-01-01T00:00:30Z"), ended.getLastHeartbeatAt());
    }

    @Test
    void countActiveSessionsIgnoresEnded() {
        InMemoryStreamSessionRepository repository = new InMemoryStreamSessionRepository();
        TimeProvider timeProvider = () -> Instant.parse("2024-01-01T00:00:10Z");
        StreamSessionService service = new StreamSessionService(repository, timeProvider);

        StreamSession sessionOne = service.startSession("stream-1", "user-1");
        service.startSession("stream-1", "user-2");

        service.endSession(sessionOne.getSessionId());

        assertEquals(1, service.countActiveSessions("stream-1"));
    }

    @Test
    void invalidInputsAreRejected() {
        StreamSessionService service = new StreamSessionService(
                new InMemoryStreamSessionRepository(),
                Instant::now
        );

        IllegalArgumentException invalidStream = assertThrows(
                IllegalArgumentException.class,
                () -> service.startSession(" ", "user-1")
        );
        assertNotNull(invalidStream.getMessage());

        IllegalArgumentException notFound = assertThrows(
                IllegalArgumentException.class,
                () -> service.heartbeat(UUID.randomUUID())
        );
        assertNotNull(notFound.getMessage());
    }

    private static class InMemoryStreamSessionRepository implements StreamSessionRepository {
        private final ConcurrentHashMap<UUID, StreamSession> sessions = new ConcurrentHashMap<>();

        @Override
        public StreamSession save(StreamSession session) {
            sessions.put(session.getSessionId(), session);
            return session;
        }

        @Override
        public Optional<StreamSession> findById(UUID sessionId) {
            return Optional.ofNullable(sessions.get(sessionId));
        }

        @Override
        public List<StreamSession> findByStreamId(StreamId streamId) {
            List<StreamSession> result = new ArrayList<>();
            for (StreamSession session : sessions.values()) {
                if (session.getStreamId().equals(streamId)) {
                    result.add(session);
                }
            }
            return result;
        }
    }
}
