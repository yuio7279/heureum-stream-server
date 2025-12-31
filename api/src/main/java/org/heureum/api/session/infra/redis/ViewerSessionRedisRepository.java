package org.heureum.api.session.infra.redis;

import org.heureum.core.domain.session.ViewerSession;
import org.heureum.core.domain.stream.StreamId;
import org.heureum.core.domain.user.ViewerId;
import org.heureum.core.repository.session.ViewerSessionRepository;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ViewerSessionRedisRepository implements ViewerSessionRepository {
    private static final String VIEWER_KEY_PREFIX = "viewer:stream_id:";
    private static final String VIEWERS_SET_PREFIX = "stream:";
    private static final String FIELD_STREAM_ID = "streamId";
    private static final String FIELD_VIEWER_ID = "viewerId";
    private static final String FIELD_CONNECTED_AT = "connectedAt";
    private static final String FIELD_LAST_HEARTBEAT_AT = "lastHeartbeatAt";

    private final StringRedisTemplate redisTemplate;
    private final HashOperations<String, String, String> hashOps;
    private final SetOperations<String, String> setOps;

    public ViewerSessionRedisRepository(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.hashOps = redisTemplate.opsForHash();
        this.setOps = redisTemplate.opsForSet();
    }

    @Override
    public ViewerSession save(ViewerSession session, Duration ttl) {
        String viewerKey = viewerKey(session.getStreamId(), session.getViewerId());
        String viewersKey = viewersKey(session.getStreamId());
        Map<String, String> values = new HashMap<>();
        values.put(FIELD_STREAM_ID, session.getStreamId().value());
        values.put(FIELD_VIEWER_ID, session.getViewerId().value());
        values.put(FIELD_CONNECTED_AT, String.valueOf(session.getConnectedAt().toEpochMilli()));
        values.put(FIELD_LAST_HEARTBEAT_AT, String.valueOf(session.getLastHeartbeatAt().toEpochMilli()));
        hashOps.putAll(viewerKey, values);
        redisTemplate.expire(viewerKey, ttl);
        setOps.add(viewersKey, session.getViewerId().value());
        return session;
    }

    @Override
    public Optional<ViewerSession> findByStreamIdAndViewerId(StreamId streamId, ViewerId viewerId) {
        String viewerKey = viewerKey(streamId, viewerId);
        Map<String, String> entries = hashOps.entries(viewerKey);
        if (entries == null || entries.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(ViewerSession.restore(
                StreamId.of(entries.get(FIELD_STREAM_ID)),
                ViewerId.of(entries.get(FIELD_VIEWER_ID)),
                Instant.ofEpochMilli(Long.parseLong(entries.get(FIELD_CONNECTED_AT))),
                Instant.ofEpochMilli(Long.parseLong(entries.get(FIELD_LAST_HEARTBEAT_AT)))
        ));
    }

    @Override
    public void delete(StreamId streamId, ViewerId viewerId) {
        redisTemplate.delete(viewerKey(streamId, viewerId));
        setOps.remove(viewersKey(streamId), viewerId.value());
    }

    private String viewerKey(StreamId streamId, ViewerId viewerId) {
        return VIEWER_KEY_PREFIX + streamId.value() + ":viewer_id:" + viewerId.value();
    }

    private String viewersKey(StreamId streamId) {
        return VIEWERS_SET_PREFIX + streamId.value() + ":viewers";
    }
}
