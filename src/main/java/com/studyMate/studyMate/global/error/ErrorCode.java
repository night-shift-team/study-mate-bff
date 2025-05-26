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

    INVALID_GOOGLE_AUTH_CODE(400, "invalid google auth code", "0106"),
    INVALID_GITHUB_AUTH_CODE(400, "invalid github auth code", "0107"),

    INVALID_USERID(400, "invalid user id", "0108"),
    DUP_NICKNAME(400, "nickname already exist", "0109"),


    /**
     * Question Errors
     * 0400 ~ 0499
     */
    INVALID_ANSWERSHEET(409, "invalid answer sheet", "0400"),
    INVALID_QUESTION(400, "invalid question", "0401"),
    NO_QUESTION_RECORDS(400, "no question records, retry after finish the question", "0402"),
    NO_LEVEL_TEST_RECORDS(400, "no level test records, retry after finish the level test", "0403"),
    INVALID_QUESTION_CATEOGRY(400, "invalid question category", "0404"),
    DUP_QUESTION(400, "question already exist (question title)", "0405"),
    NO_AVAILIABLE_QEUSTION(404, "no available question", "0406"),
    EXCEED_DAILY_QUESTION_LIMIT(400, "the category daily question limit exceeded", "0407"),

    /**
     * Notice Errors
     * 0500 ~ 0599
     */
    INVALID_NOTICE_ID(400, "invalid notice id", "0500"),


    /**
     * Notice Errors
     * 0600 ~ 0699
     */
    INVALID_BOARD_ID(400, "invalid board id", "0500")

    ;

    private final int status;
    private final String message;
    private final String eCode;
}
