package com.verong.demo.database.custom.hibernate;

import com.verong.demo.database.custom.hibernate.persistence.model.Student;
import com.verong.demo.database.custom.hibernate.persistence.orm.MyEntityManager;
import com.verong.demo.database.custom.hibernate.persistence.orm.MyEntityManagerImpl;

import javax.sql.DataSource;

public class Application {

    private static final String DATASOURCE_URL = "";
    private static final String DATASOURCE_USER = "";
    private static final String DATASOURCE_PASSWORD = "";

    public static void main(String[] args) {

        /*
            Your main task is to complete several points:
            - use the database from the previous exercise;
            - insert some student to have one to search;
            - configure DataSource properly (do not forget about JDBC Driver);
            - implement the method MyEntityManager.findById(Class<T> type, Long id) (see more in the method):
            - test you method if it works correctly;
        */

        // TODO: initialize and configure your DataSource
        DataSource dataSource = null;

        // TODO: implement and initialize your implementation to use it later in a code
        MyEntityManager myEntityManager = new MyEntityManagerImpl(dataSource);

        var student = myEntityManager.findById(Student.class, 1L);

        System.out.println("Student (found by MyEntityManager): %s".formatted(student));
    }
}
