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
    private final Instant createdAt;
    private Instant startedAt;
    private Instant endedAt;

    /**
     * 생성용 생성자 (신규 생성)
     */
    private Stream(StreamId id, StreamKey streamKey, String title) {
        this(
                id,
                streamKey,
                title,
                StreamStatus.CREATED,
                Instant.now(),
                null,
                null
        );
    }

    /**
     * 복원용 생성자 (DB 로딩)
     */
    private Stream(StreamId id,
                   StreamKey streamKey,
                   String title,
                   StreamStatus status,
                   Instant createdAt,
                   Instant startedAt,
                   Instant endedAt) {

        this.id = Objects.requireNonNull(id);
        this.streamKey = Objects.requireNonNull(streamKey);
        this.title = requireTitle(title);

        this.status = Objects.requireNonNull(status);
        this.createdAt = Objects.requireNonNull(createdAt);
        this.startedAt = startedAt;
        this.endedAt = endedAt;
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

    public static Stream restore(StreamId id,
                                 StreamKey key,
                                 String title,
                                 StreamStatus status,
                                 Instant createdAt,
                                 Instant startedAt,
                                 Instant endedAt) {
        return new Stream(id, key, title, status, createdAt, startedAt, endedAt);
    }

}
