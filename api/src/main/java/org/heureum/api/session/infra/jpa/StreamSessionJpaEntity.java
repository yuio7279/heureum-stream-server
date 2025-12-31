package org.heureum.api.session.infra.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.heureum.core.domain.session.StreamSessionStatus;

import java.time.Instant;
import java.util.UUID;

@Getter
@Entity
@Table(name = "stream_sessions")
@NoArgsConstructor
public class StreamSessionJpaEntity {

    @Id
    @Column(nullable = false, updatable = false)
    private UUID sessionId;

    @Column(nullable = false)
    private String streamId;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private Instant startedAt;

    @Column(nullable = false)
    private Instant lastHeartbeatAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StreamSessionStatus status;

    public StreamSessionJpaEntity(UUID sessionId,
                                  String streamId,
                                  String userId,
                                  Instant startedAt,
                                  Instant lastHeartbeatAt,
                                  StreamSessionStatus status) {
        this.sessionId = sessionId;
        this.streamId = streamId;
        this.userId = userId;
        this.startedAt = startedAt;
        this.lastHeartbeatAt = lastHeartbeatAt;
        this.status = status;
    }
}
