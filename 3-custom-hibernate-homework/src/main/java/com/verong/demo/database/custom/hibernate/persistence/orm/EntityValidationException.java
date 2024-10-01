package com.verong.demo.database.custom.hibernate.persistence.orm;

public class EntityValidationException extends RuntimeException {
    public EntityValidationException(String message) {
        super(message);
    }

    public EntityValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
