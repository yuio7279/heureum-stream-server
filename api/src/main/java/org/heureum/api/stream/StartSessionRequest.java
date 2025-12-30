package org.heureum.api.stream;

import jakarta.validation.constraints.NotBlank;

public record StartSessionRequest(@NotBlank String userId) {
}
