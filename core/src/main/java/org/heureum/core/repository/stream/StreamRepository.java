package org.heureum.core.repository.stream;

import org.heureum.core.domain.stream.Stream;
import org.heureum.core.domain.stream.StreamId;
import org.heureum.core.domain.stream.StreamKey;

import java.util.Optional;

public interface StreamRepository {
    Stream save(Stream stream);

    Optional<Stream> findById(StreamId id);

    Optional<Stream> findByStreamKey(StreamKey streamKey);
}
