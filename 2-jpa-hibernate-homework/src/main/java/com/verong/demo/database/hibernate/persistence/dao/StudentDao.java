package com.verong.demo.database.hibernate.persistence.dao;

import com.verong.demo.database.hibernate.persistence.dto.StudentIdAndNameDto;
import com.verong.demo.database.hibernate.persistence.model.Student;
import com.verong.demo.database.hibernate.persistence.model.StudentStatus;

import java.util.List;
import java.util.Optional;

/**
 * The Data Access Object (DAO) pattern is used to isolate a logic of working
 * with a database in a separate application/business layer. It allows to expose
 * to a user methods of creating, reading, updating, and deleting for some entity,
 * but hide all complex logic of performing CRUD operation to a certain database
 * using any persistence API (such as JDBC  API, JPA and so on).
 * <br>
 * <br>
 * {@link StudentDao} is an interface with list of method for CRUD operations
 * that can be used for the entity {@link Student}.
 */
public interface StudentDao {

    /**
     * This method stores a new {@link Student} to a database and sets generated id
     * to {@link Student} object back.
     *
     * @param student an object of {@link Student} that has to be stored in a database
     */
    void create(Student student);

    /**
     * This method returns {@code Optional} with {@link Student} that has been found
     * in a database by ID
     *
     * @param id primary key of the {@link Student} entity
     * @return an {@code Optional} with a found {@link Student} or with {@code null}
     * if a student with request id has not been found
     */
    Optional<Student> findById(Long id);

    /**
     * This method returns {@link List} of all {@link Student}
     *
     * @return a list of {@link Student}
     */
    List<Student> findAll();

    /**
     * This method returns {@link List} of all {@link Student} which have the column 'scholarship' with passed value.
     * IMPORTANT: this method has to be implemented using EntityManager and JPQL;
     *
     * @param scholarship a boolean value that indicates whether a student has a scholarship or does not have.
     */
    List<StudentIdAndNameDto> findByScholarshipUsingJPQL(Boolean scholarship);

    /**
     * This method returns {@link List} of all {@link Student} which have the column 'scholarship' with passed value.
     * IMPORTANT: this method has to be implemented using Criteria API;
     *
     * @param scholarship a boolean value that indicates whether a student has a scholarship or does not have.
     */
    List<StudentIdAndNameDto> findByScholarshipUsingCriteriaApi(Boolean scholarship);

    /**
     *
     * This method finds a {@link Student} in a database and updates the status of the found {@link Student}.
     *
     * @throws java.util.NoSuchElementException if a student is not found by passed ID.
     *
     * @param id student ID.
     * @param status {@link StudentStatus} that has to be set to the request {@link Student}.
     */
    void updateStatus(Long id, StudentStatus status);

    /**
     * This method deletes all student's data from a database using the value of ID of the passed {@link Student}.
     * The student passed into the method entity must already be stored in a database.
     * Otherwise, the method should throw an {@code Exception} (can be custom type) to notify that
     * an entity with such ID does not exist in a database.
     *
     * @param student an object of {@link Student} that has to be deleted from a database
     */
    void remove(Student student);
}
