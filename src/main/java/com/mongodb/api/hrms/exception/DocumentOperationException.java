package com.mongodb.api.hrms.exception;

public class DocumentOperationException extends RuntimeException {
    public DocumentOperationException(String message) {
        super(message);
    }
}
