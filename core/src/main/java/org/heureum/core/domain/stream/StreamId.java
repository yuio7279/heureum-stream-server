package org.heureum.core.domain.stream;

import java.util.Objects;
import java.util.UUID;

public final class StreamId {
    private final String value;

    private StreamId(String value) {
        this.value = Objects.requireNonNull(value);
    }

    public static StreamId newId() {
        return new StreamId(UUID.randomUUID().toString());
    }

    public static StreamId of(String value) {
        return new StreamId(value);
    }

    public String value() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
}

