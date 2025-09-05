package com.studyMate.studyMate.global.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.IOException;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {CustomException.class})
    @ResponseBody
    protected ResponseEntity<ErrorResponse> handleCustomException(CustomException e) {
        var ec = e.getErrorCode();

        log.error("[❌custom exception] status : {} ||| [{}] {}", ec.getStatus(), ec.getECode(), ec.getMessage());

        return ResponseEntity.status(ec.getStatus())
                .body(new ErrorResponse(ec.getStatus(), ec.getMessage(), ec.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException e) {
        var ec = ErrorCode.INVALID_AURGUMENT;

        log.warn("[❌invalid arg exception] status : {} ||| [{}] {}", ec.getStatus(), ec.getECode(), e.getMessage());

        return ResponseEntity.status(ec.getStatus())
                .body(new ErrorResponse(ec.getStatus(), ec.getECode(), e.getMessage()));
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ErrorResponse> handleNpe(NullPointerException e) {
        var ec = ErrorCode.NULL_POINTER;

        log.error("[❌null pointer exception] status : {} ||| [{}] {}", ec.getStatus(), ec.getECode(), e.getMessage());

        return ResponseEntity.status(ec.getStatus())
                .body(new ErrorResponse(ec.getStatus(), ec.getECode(), e.getMessage()));
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<ErrorResponse> handleIo(IOException e) {
        var ec = ErrorCode.IO_EXCEPTION;

        log.error("[❌io exception] status : {} ||| [{}] {}", ec.getStatus(), ec.getECode(), e.getMessage());

        return ResponseEntity.status(ec.getStatus())
                .body(new ErrorResponse(ec.getStatus(), ec.getECode(), e.getMessage()));
    }
}