package org.heureum.core.service.stream;

import org.heureum.common.exception.BusinessException;
import org.heureum.core.domain.stream.Stream;
import org.heureum.core.domain.stream.StreamStatus;
import org.heureum.core.support.InMemoryStreamRepository;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StreamServiceTest {

    @Test
    void create_saves_stream() {
        var repo = new InMemoryStreamRepository();
        var service = new StreamService(repo);

        Stream created = service.create("title");

        assertEquals(StreamStatus.CREATED, created.status());
        assertEquals(created, repo.findById(created.id()).orElseThrow());
    }

    @Test
    void startByStreamKey_transitions_to_live() {
        var repo = new InMemoryStreamRepository();
        var service = new StreamService(repo);

        Stream created = service.create("t");

        service.startByStreamKey(created.streamKey().value());

        Stream loaded = repo.findById(created.id()).orElseThrow();
        assertEquals(StreamStatus.LIVE, loaded.status());
    }

    @Test
    void endByStreamKey_transitions_to_ended() {
        var repo = new InMemoryStreamRepository();
        var service = new StreamService(repo);

        Stream created = service.create("t");
        service.startByStreamKey(created.streamKey().value());
        service.endByStreamKey(created.streamKey().value());

        Stream loaded = repo.findById(created.id()).orElseThrow();
        assertEquals(StreamStatus.ENDED, loaded.status());
    }

    @Test
    void startByStreamKey_throws_if_not_found() {
        var repo = new InMemoryStreamRepository();
        var service = new StreamService(repo);

        assertThrows(BusinessException.class, () -> service.startByStreamKey("nope"));
    }
}
