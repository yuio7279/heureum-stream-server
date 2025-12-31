package org.heureum.common.time;

import java.time.Clock;
import java.time.Instant;

public class SystemTimeProvider implements TimeProvider {
    private final Clock clock;

    public SystemTimeProvider(Clock clock) {
        this.clock = clock;
    }

    public static SystemTimeProvider utc() {
        return new SystemTimeProvider(Clock.systemUTC());
    }

    @Override
    public Instant now() {
        return Instant.now(clock);
    }
}
