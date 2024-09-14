package com.verong.demo.database.jdbc;

import com.verong.demo.database.jdbc.persistence.dao.StudentDao;
import com.verong.demo.database.jdbc.persistence.model.Student;
import com.verong.demo.database.jdbc.persistence.model.StudentStatus;

import javax.sql.DataSource;
import java.time.LocalDate;

public class Application {

    private static final String DATASOURCE_URL = "";
    private static final String DATASOURCE_USER = "";
    private static final String DATASOURCE_PASSWORD = "";

    public static void main(String[] args) {

        /*
            Your main task is to complete several points:
            - install a database on your computer using comfortable way and run database to be able to work it;
            - configure DataSource properly (do not forget about JDBC Driver);
            - create your implementation of StudentDao interface using JDBC to manage Student entity;
            - add more code with interaction with other method of StudentDao in class Application in method "main";
        */

        // TODO: initialize and configure your DataSource
        DataSource dataSource = null;

        // TODO: implement and initialize your implementation to use it later in a code
        StudentDao studentDao = null;

        /* An example of how to use DAO to store a new student in database */
        var newStudent = Student.builder()
                .email("roberto.nash@mail.com")
                .firstName("Roberto")
                .lastName("Nash")
                .birthday(LocalDate.of(2001, 1, 1))
                .scholarship(true)
                .status(StudentStatus.ACTIVE)
                .build();

        studentDao.create(newStudent);

        // TODO: add more examples with other methods of the StudentDao
    }
}
