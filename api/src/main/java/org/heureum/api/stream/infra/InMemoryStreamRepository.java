package org.heureum.api.stream.infra;

import org.heureum.core.domain.stream.Stream;
import org.heureum.core.domain.stream.StreamId;
import org.heureum.core.domain.stream.StreamKey;
import org.heureum.core.repository.stream.StreamRepository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryStreamRepository implements StreamRepository {
    private final Map<String, Stream> byId = new ConcurrentHashMap<>();
    private final Map<String, Stream> byKey = new ConcurrentHashMap<>();

    @Override
    public Stream save(Stream stream) {
        byId.put(stream.id().value(), stream);
        byKey.put(stream.streamKey().value(), stream);
        return stream;
    }

    @Override
    public Optional<Stream> findById(StreamId id) {
        return Optional.ofNullable(byId.get(id.value()));
    }

    @Override
    public Optional<Stream> findByStreamKey(StreamKey streamKey) {
        return Optional.ofNullable(byKey.get(streamKey.value()));
    }
}
