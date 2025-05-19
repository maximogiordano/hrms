package com.mongodb.api.hrms.advice;

import com.mongodb.api.hrms.dto.ErrorDto;
import com.mongodb.api.hrms.dto.ErrorItemDto;
import com.mongodb.api.hrms.exception.DocumentNotFoundException;
import com.mongodb.api.hrms.exception.DocumentOperationException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class GlobalExceptionHandlerTest {
    GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler();

    @Test
    void handleHttpMessageNotReadableException() {
        int code = GlobalExceptionHandler.HTTP_MESSAGE_NOT_READABLE_ERROR_CODE;
        String message = "JSON parse error: Unexpected character ('\"' (code 34)): " +
                "was expecting comma to separate Object entries";
        ErrorItemDto errorItemDto = new ErrorItemDto(code, message);
        List<ErrorItemDto> errors = List.of(errorItemDto);
        ErrorDto body = new ErrorDto(errors);
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ResponseEntity<ErrorDto> expected = new ResponseEntity<>(body, status);

        HttpMessageNotReadableException e = mock(HttpMessageNotReadableException.class);
        when(e.getMessage()).thenReturn(message);

        ResponseEntity<ErrorDto> response = globalExceptionHandler.handleHttpMessageNotReadableException(e);

        verify(e).getMessage();
        assertEquals(expected, response);
    }

    @Test
    void handleConstraintViolationException() {
        int code = GlobalExceptionHandler.CONSTRAINT_VIOLATION_ERROR_CODE;
        String message1 = "username must not be null";
        String message2 = "password must not be null";
        ErrorItemDto errorItemDto1 = new ErrorItemDto(code, message1);
        ErrorItemDto errorItemDto2 = new ErrorItemDto(code, message2);
        Set<ErrorItemDto> errors = Set.of(errorItemDto1, errorItemDto2);

        ConstraintViolationException e = mock(ConstraintViolationException.class);
        ConstraintViolation<?> constraintViolation1 = mock(ConstraintViolation.class);
        ConstraintViolation<?> constraintViolation2 = mock(ConstraintViolation.class);

        when(e.getConstraintViolations()).thenReturn(Set.of(constraintViolation1, constraintViolation2));
        when(constraintViolation1.getMessage()).thenReturn(message1);
        when(constraintViolation2.getMessage()).thenReturn(message2);

        ResponseEntity<ErrorDto> response = globalExceptionHandler.handleConstraintViolationException(e);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(errors, new HashSet<>(response.getBody().getErrors()));
    }

    @Test
    void handleBadCredentialsException() {
        int code = GlobalExceptionHandler.BAD_CREDENTIALS_ERROR_CODE;
        String message = "Bad credentials";
        ErrorItemDto errorItemDto = new ErrorItemDto(code, message);
        List<ErrorItemDto> errors = List.of(errorItemDto);
        ErrorDto body = new ErrorDto(errors);
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        ResponseEntity<ErrorDto> expected = new ResponseEntity<>(body, status);

        BadCredentialsException e = mock(BadCredentialsException.class);
        when(e.getMessage()).thenReturn(message);

        ResponseEntity<ErrorDto> response = globalExceptionHandler.handleBadCredentialsException(e);

        verify(e).getMessage();
        assertEquals(expected, response);
    }

    @Test
    void handleDocumentNotFoundException() {
        int code = GlobalExceptionHandler.DOCUMENT_NOT_FOUND_ERROR_CODE;
        String message = "employee not found";
        ErrorItemDto errorItemDto = new ErrorItemDto(code, message);
        List<ErrorItemDto> errors = List.of(errorItemDto);
        ErrorDto body = new ErrorDto(errors);
        HttpStatus status = HttpStatus.NOT_FOUND;
        ResponseEntity<ErrorDto> expected = new ResponseEntity<>(body, status);

        DocumentNotFoundException e = mock(DocumentNotFoundException.class);
        when(e.getMessage()).thenReturn(message);

        ResponseEntity<ErrorDto> response = globalExceptionHandler.handleDocumentNotFoundException(e);

        verify(e).getMessage();
        assertEquals(expected, response);
    }

    @Test
    void handleDocumentOperationException() {
        int code = GlobalExceptionHandler.DOCUMENT_OPERATION_ERROR_CODE;
        String message = "insufficient balance";
        ErrorItemDto errorItemDto = new ErrorItemDto(code, message);
        List<ErrorItemDto> errors = List.of(errorItemDto);
        ErrorDto body = new ErrorDto(errors);
        HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
        ResponseEntity<ErrorDto> expected = new ResponseEntity<>(body, status);

        DocumentOperationException e = mock(DocumentOperationException.class);
        when(e.getMessage()).thenReturn(message);

        ResponseEntity<ErrorDto> response = globalExceptionHandler.handleDocumentOperationException(e);

        verify(e).getMessage();
        assertEquals(expected, response);
    }

    @Test
    void handleNoResourceFoundException() {
        int code = GlobalExceptionHandler.NO_RESOURCE_FOUND_ERROR_CODE;
        String message = "No static resource leave.";
        ErrorItemDto errorItemDto = new ErrorItemDto(code, message);
        List<ErrorItemDto> errors = List.of(errorItemDto);
        ErrorDto body = new ErrorDto(errors);
        HttpStatus status = HttpStatus.NOT_FOUND;
        ResponseEntity<ErrorDto> expected = new ResponseEntity<>(body, status);

        NoResourceFoundException e = mock(NoResourceFoundException.class);
        when(e.getMessage()).thenReturn(message);

        ResponseEntity<ErrorDto> response = globalExceptionHandler.handleNoResourceFoundException(e);

        verify(e).getMessage();
        assertEquals(expected, response);
    }

    @Test
    void handleAuthorizationDeniedException() {
        int code = GlobalExceptionHandler.AUTHORIZATION_DENIED_ERROR_CODE;
        String message = "Access Denied";
        ErrorItemDto errorItemDto = new ErrorItemDto(code, message);
        List<ErrorItemDto> errors = List.of(errorItemDto);
        ErrorDto body = new ErrorDto(errors);
        HttpStatus status = HttpStatus.FORBIDDEN;
        ResponseEntity<ErrorDto> expected = new ResponseEntity<>(body, status);

        AuthorizationDeniedException e = mock(AuthorizationDeniedException.class);
        when(e.getMessage()).thenReturn(message);

        ResponseEntity<ErrorDto> response = globalExceptionHandler.handleAuthorizationDeniedException(e);

        verify(e).getMessage();
        assertEquals(expected, response);
    }

    @Test
    void handleException() {
        int code = GlobalExceptionHandler.UNKNOWN_ERROR_CODE;
        String message = "org.springframework.security.authentication.InternalAuthenticationServiceException: " +
                "Exception authenticating MongoCredential{mechanism=SCRAM-SHA-1, userName='admin', source='admin', " +
                "password=<hidden>, mechanismProperties=<hidden>}";
        ErrorItemDto errorItemDto = new ErrorItemDto(code, message);
        List<ErrorItemDto> errors = List.of(errorItemDto);
        ErrorDto body = new ErrorDto(errors);
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        ResponseEntity<ErrorDto> expected = new ResponseEntity<>(body, status);

        Exception e = mock(Exception.class);
        when(e.toString()).thenReturn(message);

        ResponseEntity<ErrorDto> response = globalExceptionHandler.handleException(e);

        assertEquals(expected, response);
    }
}
