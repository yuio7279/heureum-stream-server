package org.heureum.core.domain.user;

import java.util.Objects;
import java.util.UUID;

public final class ViewerId {
    private final String value;

    private ViewerId(String value) {
        this.value = Objects.requireNonNull(value, "value");
    }

    public static ViewerId newId() {
        return new ViewerId(UUID.randomUUID().toString());
    }

    public static ViewerId of(String value) {
        return new ViewerId(value);
    }

    public String value() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
}
