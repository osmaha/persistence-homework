package com.verong.demo.database.custom.hibernate;

import com.verong.demo.database.custom.hibernate.persistence.model.Student;
import com.verong.demo.database.custom.hibernate.persistence.orm.MyEntityManager;
import com.verong.demo.database.custom.hibernate.persistence.orm.MyEntityManagerImpl;
import org.postgresql.ds.PGSimpleDataSource;

public class Application {

    private static final String DATASOURCE_URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String DATASOURCE_USER = "postgres";
    private static final String DATASOURCE_PASSWORD = "postgres";

    public static void main(String[] args) {

        /*
            Your main task is to complete several points:
            - use the database from the previous exercise;
            - insert some student to have one to search;
            - configure DataSource properly (do not forget about JDBC Driver);
            - implement the method MyEntityManager.findById(Class<T> type, Long id) (see more in the method):
            - test you method if it works correctly;
        */

        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setURL(DATASOURCE_URL);
        dataSource.setUser(DATASOURCE_USER);
        dataSource.setPassword(DATASOURCE_PASSWORD);

        MyEntityManager myEntityManager = new MyEntityManagerImpl(dataSource);

        var student = myEntityManager.findById(Student.class, 2L);

        System.out.println("Student (found by MyEntityManager): %s".formatted(student));
    }
}
