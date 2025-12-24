package org.heureum.common.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
@Builder
public class ErrorResponse {

    private final int status;
    private final String code;
    private final String errorMessage;

    public ErrorResponse of(ErrorCode errorCode) {
        return ErrorResponse.builder()
                .status(errorCode.getStatus())
                .code(errorCode.getCode())
                .errorMessage(errorCode.getMessage())
                .build();
    }
}
