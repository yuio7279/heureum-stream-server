package org.heureum.api.session.api.dto;

import jakarta.validation.constraints.NotBlank;

public record StartSessionRequest(@NotBlank String userId) {
}
