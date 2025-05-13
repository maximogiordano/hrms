package com.mongodb.api.hrms.exception;

public class EntityOperationException extends RuntimeException {
    public EntityOperationException(String message) {
        super(message);
    }
}
