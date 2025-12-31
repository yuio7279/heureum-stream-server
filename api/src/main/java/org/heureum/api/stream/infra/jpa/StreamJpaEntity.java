package org.heureum.api.stream.infra.jpa;

import jakarta.persistence.*;
import lombok.*;
import org.heureum.core.domain.stream.StreamStatus;

import java.time.Instant;

@Getter
@Entity
@Table(name = "streams")
public class StreamJpaEntity {

    @Id
    @Column(length = 64)
    private String id;

    @Column(nullable = false, unique = true, length = 64)
    private String streamKey;

    @Column(nullable = false)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 16)
    private StreamStatus status;

    @Column(nullable = false)
    private Instant createdAt;

    @Setter
    private Instant startedAt;
    @Setter
    private Instant endedAt;

    protected StreamJpaEntity() {}

    public StreamJpaEntity(String id, String streamKey, String title, StreamStatus status,
                           Instant createdAt, Instant startedAt, Instant endedAt) {
        this.id = id;
        this.streamKey = streamKey;
        this.title = title;
        this.status = status;
        this.createdAt = createdAt;
        this.startedAt = startedAt;
        this.endedAt = endedAt;
    }

}
