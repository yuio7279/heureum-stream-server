package org.heureum.core.domain.stream;

import org.heureum.common.exception.BusinessException;
import org.heureum.common.exception.ErrorCode;

import java.time.Instant;
import java.util.Objects;

public final class Stream {

    private final StreamId id;
    private final StreamKey streamKey;
    private final String title;

    private StreamStatus status;
    private Instant createdAt;
    private Instant startedAt;
    private Instant endedAt;

    private Stream(StreamId id, StreamKey streamKey, String title) {
        this.id = Objects.requireNonNull(id);
        this.streamKey = Objects.requireNonNull(streamKey);
        this.title = requireTitle(title);

        this.status = StreamStatus.CREATED;
        this.createdAt = Instant.now();
    }

    public static Stream create(String title) {
        return new Stream(StreamId.newId(), StreamKey.newKey(), title);
    }

    public void start() {
        if (status != StreamStatus.CREATED) {
            throw new BusinessException(ErrorCode.INVALID_STREAM_STATE);
        }
        this.status = StreamStatus.LIVE;
        this.startedAt = Instant.now();
    }

    public void end() {
        if (status != StreamStatus.LIVE) {
            throw new BusinessException(ErrorCode.INVALID_STREAM_STATE);
        }
        this.status = StreamStatus.ENDED;
        this.endedAt = Instant.now();
    }

    public boolean isLive() {
        return status == StreamStatus.LIVE;
    }

    public StreamId id() { return id; }
    public StreamKey streamKey() { return streamKey; }
    public String title() { return title; }
    public StreamStatus status() { return status; }
    public Instant createdAt() { return createdAt; }
    public Instant startedAt() { return startedAt; }
    public Instant endedAt() { return endedAt; }

    private static String requireTitle(String title) {
        if (title == null || title.isBlank()) {
            throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE); // 네 ErrorCode에 맞게 조정
        }
        return title.trim();
    }

    public static Stream restore(StreamId id, StreamKey key, String title,
                                 StreamStatus status, Instant createdAt,
                                 Instant startedAt, Instant endedAt) {
        Stream s = new Stream(id, key, title); // 생성자 접근 조정 필요(패키지-프라이빗 등)
        s.status = status;
        s.createdAt = createdAt;
        s.startedAt = startedAt;
        s.endedAt = endedAt;
        return s;
    }

}
