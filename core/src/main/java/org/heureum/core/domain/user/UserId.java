package org.heureum.core.domain.user;

import java.util.Objects;
import java.util.UUID;

public final class UserId {
    private final String value;

    private UserId(String value) {
        this.value = Objects.requireNonNull(value, "value");
    }

    public static UserId newId() {
        return new UserId(UUID.randomUUID().toString());
    }

    public static UserId of(String value) {
        return new UserId(value);
    }

    public String value() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
}
