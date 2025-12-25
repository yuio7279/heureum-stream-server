package org.heureum.core.domain.stream;


import java.util.Objects;
import java.util.UUID;

public final class StreamKey {
    private final String value;

    private StreamKey(String value) {
        this.value = Objects.requireNonNull(value);
    }

    public static StreamKey newKey() {
        // OBS에 넣기 쉬운 형태로 (- 제거)
        return new StreamKey(UUID.randomUUID().toString().replace("-", ""));
    }

    public static StreamKey of(String value) {
        return new StreamKey(value);
    }

    public String value() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
}

