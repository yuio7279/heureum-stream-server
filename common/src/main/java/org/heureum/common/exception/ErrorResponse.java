package org.heureum.common.exception;

import lombok.Builder;

import java.time.Instant;

@Builder
public record ErrorResponse(String code, String errorMessage, String traceId, String path, Instant timestamp) {

    public static ErrorResponse of(ErrorCode errorCode, String traceId, String path) {
        return ErrorResponse.builder()
                .code(errorCode.getCode())
                .errorMessage(errorCode.getMessage())
                .traceId(traceId)
                .path(path)
                .build();
    }
}
