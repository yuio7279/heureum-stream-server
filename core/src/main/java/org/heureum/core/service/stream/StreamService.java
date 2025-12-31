package org.heureum.core.service.stream;

import org.heureum.common.exception.BusinessException;
import org.heureum.common.exception.ErrorCode;
import org.heureum.core.domain.stream.Stream;
import org.heureum.core.domain.stream.StreamId;
import org.heureum.core.domain.stream.StreamKey;
import org.heureum.core.repository.stream.StreamRepository;

public class StreamService {

    private final StreamRepository streamRepository;

    public StreamService(StreamRepository streamRepository) {
        this.streamRepository = streamRepository;
    }

    public Stream create(String title) {
        Stream stream = Stream.create(title);
        return streamRepository.save(stream);
    }

    public Stream get(StreamId streamId) {
        return streamRepository.findById(streamId)
                .orElseThrow(() -> new BusinessException(ErrorCode.STREAM_NOT_FOUND));
    }

    public void startByStreamKey(String streamKey) {
        StreamKey key = StreamKey.of(streamKey);

        Stream stream = streamRepository.findByStreamKey(key)
                .orElseThrow(() -> new BusinessException(ErrorCode.STREAM_NOT_FOUND));

        stream.start();              // 상태 전이 규칙은 Stream 내부
        streamRepository.save(stream);
    }

    public void endByStreamKey(String streamKey) {
        StreamKey key = StreamKey.of(streamKey);

        Stream stream = streamRepository.findByStreamKey(key)
                .orElseThrow(() -> new BusinessException(ErrorCode.STREAM_NOT_FOUND));

        stream.end();
        streamRepository.save(stream);
    }
}
