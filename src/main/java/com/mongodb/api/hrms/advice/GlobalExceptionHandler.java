package com.mongodb.api.hrms.advice;

import com.mongodb.api.hrms.dto.ErrorDto;
import com.mongodb.api.hrms.dto.ErrorItemDto;
import com.mongodb.api.hrms.exception.DocumentNotFoundException;
import com.mongodb.api.hrms.exception.DocumentOperationException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {
    public static final int HTTP_MESSAGE_NOT_READABLE_ERROR_CODE = 1;
    public static final int CONSTRAINT_VIOLATION_ERROR_CODE = 2;
    public static final int BAD_CREDENTIALS_ERROR_CODE = 3;
    public static final int DOCUMENT_NOT_FOUND_ERROR_CODE = 4;
    public static final int DOCUMENT_OPERATION_ERROR_CODE = 5;
    public static final int NO_RESOURCE_FOUND_ERROR_CODE = 6;
    public static final int AUTHORIZATION_DENIED_ERROR_CODE = 7;
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

    @ExceptionHandler(DocumentNotFoundException.class)
    public ResponseEntity<ErrorDto> handleDocumentNotFoundException(DocumentNotFoundException e) {
        return getSingleErrorDto(DOCUMENT_NOT_FOUND_ERROR_CODE, e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DocumentOperationException.class)
    public ResponseEntity<ErrorDto> handleDocumentOperationException(DocumentOperationException e) {
        return getSingleErrorDto(DOCUMENT_OPERATION_ERROR_CODE, e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorDto> handleNoResourceFoundException(NoResourceFoundException e) {
        return getSingleErrorDto(NO_RESOURCE_FOUND_ERROR_CODE, e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<ErrorDto> handleAuthorizationDeniedException(AuthorizationDeniedException e) {
        return getSingleErrorDto(AUTHORIZATION_DENIED_ERROR_CODE, e.getMessage(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDto> handleException(Exception e) {
        return getSingleErrorDto(UNKNOWN_ERROR_CODE, e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<ErrorDto> getSingleErrorDto(Integer code, String detail, HttpStatus status) {
        var body = new ErrorDto(List.of(new ErrorItemDto(code, detail)));
        return new ResponseEntity<>(body, status);
    }
}
