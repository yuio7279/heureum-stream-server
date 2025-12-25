package org.heureum.core.service.watch;

import org.heureum.common.exception.BusinessException;
import org.heureum.core.domain.stream.Stream;
import org.heureum.core.support.InMemoryStreamRepository;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WatchTokenServiceTest {

    @Test
    void issue_token_when_stream_is_live() {
        var repo = new InMemoryStreamRepository();
        var streamService = new org.heureum.core.service.stream.StreamService(repo);
        var tokenService = new WatchTokenService(repo);

        Stream stream = streamService.create("live");
        streamService.startByStreamKey(stream.streamKey().value());

        var token = tokenService.issue(stream.streamKey().value(), 60);

        assertNotNull(token.value());
        assertFalse(token.isExpired());
    }

    @Test
    void issue_token_fails_when_stream_not_live() {
        var repo = new InMemoryStreamRepository();
        var streamService = new org.heureum.core.service.stream.StreamService(repo);
        var tokenService = new WatchTokenService(repo);

        Stream stream = streamService.create("not-live");

        assertThrows(
                BusinessException.class,
                () -> tokenService.issue(stream.streamKey().value(), 60)
        );
    }
}
