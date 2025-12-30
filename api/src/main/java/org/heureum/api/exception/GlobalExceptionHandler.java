package org.heureum.api.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.heureum.common.exception.BusinessException;
import org.heureum.common.exception.ErrorCode;
import org.heureum.common.exception.ErrorResponse;
import org.slf4j.MDC;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 1) 비즈니스 예외
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusiness(
            BusinessException ex,
            HttpServletRequest request
    ) {
        ErrorCode ec = ex.getErrorCode();
        log.error("BusinessException code={}, msg={}",
                ex.getErrorCode(), ex.getMessage(), ex);
        return ResponseEntity
                .status(ec.getStatus())
                .body(ErrorResponse.of(ec, traceId(), request.getRequestURI()));
    }
//
//    // 2) Validation 예외 (@Valid)
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<ErrorResponse> handleValidation(
//            MethodArgumentNotValidException ex,
//            HttpServletRequest request
//    ) {
//        String msg = ex.getBindingResult().getFieldErrors().stream()
//                .map(e -> e.getField() + ": " + e.getDefaultMessage())
//                .collect(Collectors.joining(", "));
//
//        return ResponseEntity
//                .badRequest()
//                .body(ErrorResponse.of("COMMON_VALIDATION", msg, traceId(), request.getRequestURI()));
//    }
//
//    // 3) 그 외 모든 예외 (500)
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<ErrorResponse> handleUnknown(
//            Exception ex,
//            HttpServletRequest request
//    ) {
//        // 여기서 error 로그 + stacktrace 남기는 게 일반적
//        return ResponseEntity
//                .internalServerError()
//                .body(ErrorResponse.of("COMMON_500", "서버 오류가 발생했습니다.", traceId(), request.getRequestURI()));
//    }

    private String traceId() {
        // 예: Filter에서 MDC에 넣어둔 traceId 사용 (없으면 null일 수 있음)
        return MDC.get("traceId");
    }
}
