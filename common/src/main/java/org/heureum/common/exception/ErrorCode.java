package org.heureum.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    INVALID_INPUT_VALUE(400, "C001", "잘못된 입력값입니다."),
    ENTITY_NOT_FOUND(400, "C002", "찾을 수 없는 엔티티입니다."),
    INTERNAL_SERVER_ERROR(500, "C003", "서버 내부 오류입니다."),

    // 비즈니스 에러 (예시: Member)
    MEMBER_NOT_FOUND(400, "M001", "존재하지 않는 회원입니다."),
    EMAIL_DUPLICATION(400, "M002", "이미 존재하는 이메일입니다.");

    private final int status;
    private final String code;
    private final String message;

}