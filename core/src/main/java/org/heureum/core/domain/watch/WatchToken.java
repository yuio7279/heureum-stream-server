package org.heureum.core.domain.watch;

import java.time.Instant;
import java.util.UUID;

public final class WatchToken {

    private final String value;
    private final Instant expiresAt;

    private WatchToken(String value, Instant expiresAt) {
        this.value = value;
        this.expiresAt = expiresAt;
    }

    public static WatchToken issue(long ttlSeconds) {
        return new WatchToken(
                UUID.randomUUID().toString().replace("-", ""),
                Instant.now().plusSeconds(ttlSeconds)
        );
    }

    public String value() {
        return value;
    }

    public Instant expiresAt() {
        return expiresAt;
    }

    public boolean isExpired() {
        return Instant.now().isAfter(expiresAt);
    }
}
