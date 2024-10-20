package org.verong.demo.database.spring.data.jpa.runner;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.verong.demo.database.spring.data.jpa.persistence.model.Student;
import org.verong.demo.database.spring.data.jpa.persistence.model.StudentStatus;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class StudentManagementRunner implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {

        /*
            Your main task is to complete several points:
            - use the database from the previous exercise;
            - configure JPA properly (you need to add a dependency for Spring Data JPA and the JDBC Driver);
            - create several repositories with several method to manage Student entity:
                - StudentRepository 				(using JPA Repository) 	-> CRUD operation, name generated queries;
                - EntityManagerStudentRepository 	(using EntityManager) 	-> come up with custom queries for selecting;
                - JdbcTemplateStudentRepository 	(using JdbcTemplate) 	-> come up with custom queries for selecting;
            - inject StudentRepository, EntityManagerStudentRepository, and JdbcTemplateStudentRepository into this bean;
            - add more code with interaction with other method of StudentDao in class Application in lambda
              expression of CommandLineRunner bean;
         */

        /* An example of how to use DAO to store a new student in database */
        var newStudent = Student.builder()
                .email("roberto.nash@mail.com")
                .firstName("Roberto")
                .lastName("Nash")
                .birthday(LocalDate.of(2001, 1, 1))
                .scholarship(true)
                .status(StudentStatus.ACTIVE)
                .build();

        // TODO: use your repositories to interact with Student;

    }
}
