package org.heureum.core.domain.stream;

import org.heureum.common.exception.BusinessException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StreamTest {

    @Test
    void create_initial_state_is_created() {
        Stream s = Stream.create("hello");
        assertEquals(StreamStatus.CREATED, s.status());
        assertNotNull(s.id());
        assertNotNull(s.streamKey());
        assertNotNull(s.createdAt());
    }

    @Test
    void start_transitions_created_to_live() {
        Stream s = Stream.create("t");
        s.start();
        assertEquals(StreamStatus.LIVE, s.status());
        assertTrue(s.isLive());
        assertNotNull(s.startedAt());
    }

    @Test
    void end_transitions_live_to_ended() {
        Stream s = Stream.create("t");
        s.start();
        s.end();
        assertEquals(StreamStatus.ENDED, s.status());
        assertNotNull(s.endedAt());
    }

    @Test
    void cannot_end_if_not_live() {
        Stream s = Stream.create("t");
        s.start();
        assertThrows(BusinessException.class, s::start);
    }
}
