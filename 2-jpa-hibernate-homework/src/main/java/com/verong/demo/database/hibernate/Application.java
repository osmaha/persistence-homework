package com.verong.demo.database.hibernate;

import com.verong.demo.database.hibernate.persistence.dao.StudentDao;
import com.verong.demo.database.hibernate.persistence.model.Student;
import com.verong.demo.database.hibernate.persistence.model.StudentStatus;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.time.LocalDate;

public class Application {

    private static final String PERSISTENCE_UNIT_NAME = "";

    public static void main(String[] args) {

        /*
            Your main task is to complete several points:
            - use the database from the previous exercise;
            - configure Hibernate properly (do not forget about JDBC Driver, Hibernate and persistence.xml);
            - create your implementation of StudentDao interface using JPA (Hibernate implementation)
              to manage Student entity;
            - add more code with interaction with other method of StudentDao in class Application in method "main";
        */

        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);

        /*
            TODO: implement and initialize your implementation to use it later in a code

            Important: EntityManagerFactory is a thread-safe factory that is used to create an instance of EntityManager
            for each session of work with a database. It means when you create an instance of EntityManager you
            open the connection between your application and a database and within this connection, you can
            run any queries. At the end of the logic of your method, the instance of EntityManager has to
            e closed to close a previously opened connection to a database.

            Your implementation of StudentDao has to use EntityManagerFactory to create an instance of
            EntityManager in each method to work with a database.
        */

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
