package com.verong.demo.database.jdbc;

import com.verong.demo.database.jdbc.persistence.dao.StudentDao;
import com.verong.demo.database.jdbc.persistence.dao.StudentDaoImpl;
import com.verong.demo.database.jdbc.persistence.model.Student;
import com.verong.demo.database.jdbc.persistence.model.StudentStatus;
import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.util.NoSuchElementException;

public class Application {

    /* URL is set for PostgreSQL database */
    private static final String DATASOURCE_URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String DATASOURCE_USER = "postgres";
    private static final String DATASOURCE_PASSWORD = "postgres";

    public static void main(String[] args) {

        /*
            Your main task is to complete several points:
            - install a database on your computer using comfortable way and run database to be able to work it;
            - configure DataSource properly (do not forget about JDBC Driver);
            - create your implementation of StudentDao interface using JDBC to manage Student entity;
            - add more code with interaction with other method of StudentDao in class Application in method "main";
        */

        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setURL(DATASOURCE_URL);
        dataSource.setUser(DATASOURCE_USER);
        dataSource.setPassword(DATASOURCE_PASSWORD);

        StudentDao studentDao = new StudentDaoImpl(dataSource);

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

        System.out.println("Saved student: %s".formatted(newStudent)); // The student should be already with ID



        /* Find student by its id */
        var studentIdToSearch = 1L;

        var studentById = studentDao.findById(studentIdToSearch)
                .orElseThrow(() -> new NoSuchElementException("Student with id %d was not found".formatted(studentIdToSearch)));

        System.out.println("Found Student by Id %d: %s".formatted(studentIdToSearch, studentById));



        /* Find all students */
        var students = studentDao.findAll();

        System.out.println("List of found students:");
        students.forEach(System.out::println);



        /* Update student's field and update it in database */
        var studentIdToUpdate = 1L;
        var studentToUpdate = studentDao.findById(studentIdToUpdate).orElseThrow();

        studentToUpdate.setScholarship(false);
        studentToUpdate.setStatus(StudentStatus.DROPPED);

        studentDao.update(studentToUpdate); // you can check in database whether fields have been updated



        /* Remove student by id */
        var studentIdToDelete = 1L;

        var studentToDelete = studentDao.findById(studentIdToDelete).orElseThrow();
        studentDao.remove(studentToDelete);

        var exists = studentDao.findById(studentIdToDelete).isPresent();

        System.out.println("Does Student with id %d exists? %s".formatted(studentIdToDelete, exists));
    }
}
