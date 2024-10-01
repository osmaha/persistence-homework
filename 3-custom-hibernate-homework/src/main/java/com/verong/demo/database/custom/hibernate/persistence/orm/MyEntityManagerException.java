package com.verong.demo.database.custom.hibernate.persistence.orm;

public class MyEntityManagerException extends RuntimeException {
    public MyEntityManagerException(String message) {
        super(message);
    }

    public MyEntityManagerException(String message, Throwable cause) {
        super(message, cause);
    }
}
