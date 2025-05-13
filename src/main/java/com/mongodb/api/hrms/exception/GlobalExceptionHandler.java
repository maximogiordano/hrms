package com.mongodb.api.hrms.exception;

import com.mongodb.api.hrms.dto.ErrorDto;
import com.mongodb.api.hrms.dto.ErrorItemDto;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

@ControllerAdvice
@SuppressWarnings({"java:S6212", "unused"})
public class GlobalExceptionHandler {
    public static final int HTTP_MESSAGE_NOT_READABLE_ERROR_CODE = 1;
    public static final int CONSTRAINT_VIOLATION_ERROR_CODE = 2;
    public static final int BAD_CREDENTIALS_ERROR_CODE = 3;
    public static final int ENTITY_NOT_FOUND_ERROR_CODE = 4;
    public static final int ENTITY_OPERATION_ERROR_CODE = 5;
    public static final int UNKNOWN_ERROR_CODE = -1;

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorDto> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        return getSingleErrorDto(HTTP_MESSAGE_NOT_READABLE_ERROR_CODE, e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorDto> handleConstraintViolationException(ConstraintViolationException e) {
        List<ErrorItemDto> errors = e.getConstraintViolations()
                .stream()
                .map(cv -> new ErrorItemDto(CONSTRAINT_VIOLATION_ERROR_CODE, cv.getMessage()))
                .toList();

        return new ResponseEntity<>(new ErrorDto(errors), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorDto> handleBadCredentialsException(BadCredentialsException e) {
        return getSingleErrorDto(BAD_CREDENTIALS_ERROR_CODE, e.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorDto> handleEntityNotFoundException(EntityNotFoundException e) {
        return getSingleErrorDto(ENTITY_NOT_FOUND_ERROR_CODE, e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EntityOperationException.class)
    public ResponseEntity<ErrorDto> handleEntityOperationException(EntityOperationException e) {
        return getSingleErrorDto(ENTITY_OPERATION_ERROR_CODE, e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDto> handleException(Exception e) {
        return getSingleErrorDto(UNKNOWN_ERROR_CODE, e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<ErrorDto> getSingleErrorDto(Integer code, String detail, HttpStatus status) {
        ErrorDto body = new ErrorDto(List.of(new ErrorItemDto(code, detail)));
        return new ResponseEntity<>(body, status);
    }
}
