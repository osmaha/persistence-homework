package com.verong.demo.database.hibernate;

import com.verong.demo.database.hibernate.persistence.dao.StudentDao;
import com.verong.demo.database.hibernate.persistence.dao.StudentDaoImpl;
import com.verong.demo.database.hibernate.persistence.model.Student;
import com.verong.demo.database.hibernate.persistence.model.StudentStatus;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.time.LocalDate;
import java.util.NoSuchElementException;

public class Application {

    private static final String PERSISTENCE_UNIT_NAME = "default";

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
            Important: EntityManagerFactory is a thread-safe factory that is used to create an instance of EntityManager
            for each session of work with a database. It means when you create an instance of EntityManager you
            open the connection between your application and a database and within this connection, you can
            run any queries. At the end of the logic of your method, the instance of EntityManager has to
            e closed to close a previously opened connection to a database.

            Your implementation of StudentDao has to use EntityManagerFactory to create an instance of
            EntityManager in each method to work with a database.
        */

        StudentDao studentDao = new StudentDaoImpl(entityManagerFactory);

        /* An example how to use DAO to store a new student in database */

        /*
            IMPORTANT: all example use the same student

            If you want to run these examples you need to comment all except the one you want to play with
            or modify a code to insert more student to use them in examples
        */

        /* Create new student and store in the database */
        var newStudent = Student.builder()
                .email("roberto.nash@mail.com")
                .firstName("Roberto")
                .lastName("Nash")
                .birthday(LocalDate.of(2001, 1, 1))
                .scholarship(true)
                .status(StudentStatus.ACTIVE)
                .build();

        studentDao.create(newStudent);


        /* Find student by its id */
        var studentIdToSearch = 1L;

        var studentById = studentDao.findById(studentIdToSearch)
                .orElseThrow(() -> new NoSuchElementException("Student with id %d was not found".formatted(studentIdToSearch)));

        System.out.println("Found Student by Id %d: %s".formatted(studentIdToSearch, studentById));


        /* Find all students */
        var students = studentDao.findAll();

        System.out.println("List of found students:");
        students.forEach(System.out::println);


        /* Find all students that do not have scholarship (using JPQL) */
        var studentsWithoutScholarshipUsingJPQL = studentDao.findByScholarshipUsingJPQL(Boolean.FALSE);

        System.out.println("List of found students without scholarship (using JPQL):");
        studentsWithoutScholarshipUsingJPQL.forEach(System.out::println);


        /* Find all students that do not have scholarship (using Criteria API) */
        var studentsWithoutScholarshipUsingCriteriaApi = studentDao.findByScholarshipUsingCriteriaApi(Boolean.FALSE);

        System.out.println("List of found students without scholarship (using Criteria API):");
        studentsWithoutScholarshipUsingCriteriaApi.forEach(System.out::println);

        /* Update student's status by id */
        var studentIdToUpdate = 2L;
        var statusToUpdate = StudentStatus.DROPPED;

        var studentBeforeUpdate = studentDao.findById(studentIdToUpdate).orElseThrow();
        System.out.println("Student with Id %d has the following status: %s".formatted(studentIdToUpdate, studentBeforeUpdate.getStatus()));

        studentDao.updateStatus(studentIdToUpdate, statusToUpdate);

        var studentAfterUpdate = studentDao.findById(studentIdToUpdate).orElseThrow();
        System.out.println("Student with Id %d has the following status: %s".formatted(studentIdToUpdate, studentAfterUpdate.getStatus()));


        /* Remove student by id */
        var studentIdToDelete = 1L;

        var studentToDelete = studentDao.findById(studentIdToDelete).orElseThrow();
        studentDao.remove(studentToDelete);

        var exists = studentDao.findById(studentIdToDelete).isPresent();

        System.out.println("Does Student with id %d exists? %s".formatted(studentIdToDelete, exists));
    }
}
