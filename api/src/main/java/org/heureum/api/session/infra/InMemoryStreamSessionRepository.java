package org.heureum.api.session.infra;

import org.heureum.core.domain.session.StreamSession;
import org.heureum.core.repository.session.StreamSessionRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryStreamSessionRepository implements StreamSessionRepository {
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
    public List<StreamSession> findByStreamId(String streamId) {
        List<StreamSession> result = new ArrayList<>();
        for (StreamSession session : sessions.values()) {
            if (session.getStreamId().equals(streamId)) {
                result.add(session);
            }
        }
        return result;
    }
}
