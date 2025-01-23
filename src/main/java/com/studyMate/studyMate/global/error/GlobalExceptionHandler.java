package com.studyMate.studyMate.global.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.PessimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = { CustomException.class })
    @ResponseBody
    protected ResponseEntity<ErrorResponse> handleCustomException(CustomException e) {
        log.error("CustomException Catch !! Status : {} ||| [{}] {}", e.getErrorCode().getStatus(), e.getErrorCode().getECode(), e.getErrorCode().getMessage());

        ErrorResponse errorResponse = new ErrorResponse(e.getErrorCode().getStatus(), e.getErrorCode().getECode(), e.getErrorCode().getMessage());

        // TODO: EXCEPTION DB 기록

        return ResponseEntity.status(e.getErrorCode().getStatus()).body(errorResponse);
    }

    @ExceptionHandler(PessimisticLockingFailureException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<ErrorResponse> handlePessimisticLockingFailure(PessimisticLockingFailureException e) {
        ErrorCode requestConflict = ErrorCode.REQUEST_CONFLICT;
        ErrorResponse errorResponse = new ErrorResponse(requestConflict.getStatus(), requestConflict.getECode(), requestConflict.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }
}