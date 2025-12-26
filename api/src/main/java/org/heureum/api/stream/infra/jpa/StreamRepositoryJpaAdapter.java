package org.heureum.api.stream.infra.jpa;

import org.heureum.core.domain.stream.Stream;
import org.heureum.core.domain.stream.StreamId;
import org.heureum.core.domain.stream.StreamKey;
import org.heureum.core.repository.stream.StreamRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StreamRepositoryJpaAdapter implements StreamRepository {
    private final StreamJpaRepository jpa;

    public StreamRepositoryJpaAdapter(StreamJpaRepository jpa) {
        this.jpa = jpa;
    }
    @Override
    public Stream save(Stream stream) {
        StreamJpaEntity saved = jpa.save(toEntity(stream));
        return toDomain(saved);
    }

    @Override
    public Optional<Stream> findById(StreamId id) {
        return jpa.findById(id.value()).map(this::toDomain);
    }

    @Override
    public Optional<Stream> findByStreamKey(StreamKey streamKey) {
        return jpa.findByStreamKey(streamKey.value()).map(this::toDomain);
    }

    private StreamJpaEntity toEntity(Stream s) {
        return new StreamJpaEntity(
                s.id().value(),
                s.streamKey().value(),
                s.title(),
                s.status(),
                s.createdAt(),
                s.startedAt(),
                s.endedAt()
        );
    }

    private Stream toDomain(StreamJpaEntity e) {
        return Stream.restore(
                StreamId.of(e.getId()),
                StreamKey.of(e.getStreamKey()),
                e.getTitle(),
                e.getStatus(),
                e.getCreatedAt(),
                e.getStartedAt(),
                e.getEndedAt()
        );
    }
}
