package com.studyMate.studyMate.global.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    /**
     * Common Errors
     * 0001 ~ 0099
     */
    UNKNOWN_ERROR(500, "unknown error", "0001"),
    EMPTY_TOKEN(400, "empty token", "0002"),
    UNAUTHORIZED(403, "unauthorized", "0003"),
    NOT_FOUND(404, "data not found", "0004"),
    FORBIDDEN(403, "forbidden", "0005"),
    REQUEST_CONFLICT(409, "Too many requests, Please try again later", "0006"),

    /**
     * Auth Errors
     * 0100 ~ 0199
     */
    DUP_USER_ID(409, "id already exist", "0101"),
    DUP_USER_NICKNAME(409, "nickname already exist", "0102"),

    INVALID_LOGINID(400, "invalid login id", "0103"),
    INVALID_LOGINPW(400, "invalid password", "0104"),

    NOT_ACTIVE_USER(403, "not active user", "0105"),

    INVALID_GOOGLE_AUTH_CODE(403, "invalid google auth code", "0106"),

    ;
    private final int status;
    private final String message;
    private final String eCode;
}
