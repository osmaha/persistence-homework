package com.verong.demo.database.jdbc.persistence.dao;

public class StudentDaoException extends RuntimeException {
    public StudentDaoException(String message) {
        super(message);
    }

    public StudentDaoException(String message, Throwable cause) {
        super(message, cause);
    }
}
