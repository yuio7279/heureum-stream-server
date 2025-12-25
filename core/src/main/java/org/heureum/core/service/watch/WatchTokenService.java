package org.heureum.core.service.watch;

import org.heureum.common.exception.BusinessException;
import org.heureum.common.exception.ErrorCode;
import org.heureum.core.domain.stream.Stream;
import org.heureum.core.domain.stream.StreamKey;
import org.heureum.core.domain.watch.WatchToken;
import org.heureum.core.repository.stream.StreamRepository;

public class WatchTokenService {

    private final StreamRepository streamRepository;

    public WatchTokenService(StreamRepository streamRepository) {
        this.streamRepository = streamRepository;
    }

    public WatchToken issue(String streamKey, long ttlSeconds) {
        Stream stream = streamRepository.findByStreamKey(StreamKey.of(streamKey))
                .orElseThrow(() -> new BusinessException(ErrorCode.STREAM_NOT_FOUND));

        if (!stream.isLive()) {
            throw new BusinessException(ErrorCode.STREAM_NOT_LIVE);
        }

        return WatchToken.issue(ttlSeconds);
    }
}
