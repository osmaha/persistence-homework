package org.verong.demo.database.spring.data.jpa.runner;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.verong.demo.database.spring.data.jpa.persistence.model.Student;
import org.verong.demo.database.spring.data.jpa.persistence.model.StudentStatus;
import org.verong.demo.database.spring.data.jpa.persistence.model.Student_;
import org.verong.demo.database.spring.data.jpa.persistence.repository.EntityManagerStudentRepository;
import org.verong.demo.database.spring.data.jpa.persistence.repository.JdbcTemplateStudentRepository;
import org.verong.demo.database.spring.data.jpa.persistence.repository.StudentRepository;

import java.time.LocalDate;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class StudentManagementRunner implements CommandLineRunner {

    /*
     *   Processing the annotation @RequiredArgsConstructor Lombok will create a constructor with all final fields.
     *
     *   If bean has only one constructor Spring Framework will use it to inject all what it finds in the context.
     *   It means that all fields will be injected, and we will be able to use them.
     * */
    private final StudentRepository studentRepository;
    private final EntityManagerStudentRepository entityManagerStudentRepository;
    private final JdbcTemplateStudentRepository jdbcTemplateStudentRepository;

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

        var storedStudent = studentRepository.save(newStudent);

        log.info("User has been stored: {}", storedStudent);

        /* Usage of the method that has been built by keywords */
        var sortedByBirthdayStudents = studentRepository.findAllAndSort(Sort.by(Sort.Direction.ASC, Student_.BIRTHDAY));
        var stringWithSortedDates = sortedByBirthdayStudents.stream().map(Student::getBirthday).map(LocalDate::toString).collect(Collectors.joining(", "));
        log.info("Sorted birthdays: {}", stringWithSortedDates);


        /* Select students without scholarships using different approaches */
        var withoutScholarshipByJpaRepository = studentRepository.findByScholarshipUsingJPQL(false);
        log.info("Students without scholarships (JPA Repository): {}", withoutScholarshipByJpaRepository);

        var withoutScholarshipByJPQL = entityManagerStudentRepository.findByScholarshipUsingJPQL(false);
        log.info("Students without scholarships (EntityManager + JPQL): {}", withoutScholarshipByJPQL);

        var withoutScholarshipByCriteriaAPI = entityManagerStudentRepository.findByScholarshipUsingCriteriaApi(false);
        log.info("Students without scholarships (Criteria API): {}", withoutScholarshipByCriteriaAPI);

        var withoutScholarshipByJdbcTemplate = jdbcTemplateStudentRepository.findByScholarshipUsingSQL(false);
        log.info("Students without scholarships (JdbcTemplate): {}", withoutScholarshipByJdbcTemplate);
    }
}
