package org.verong.demo.database.spring.data.jpa.persistence.repository;

import org.verong.demo.database.spring.data.jpa.persistence.dto.StudentIdAndNameDto;
import org.verong.demo.database.spring.data.jpa.persistence.model.Student;

import java.util.List;

public interface EntityManagerStudentRepository {

    /*
    *   Working with Criteria API becomes easier if you use Static Metamodel instead of string names of entity fields.
    *   To be able to generate those Static Models for each entity you need to add dependency:
    *
    *   <dependency>
    *       <groupId>org.hibernate</groupId>
    *       <artifactId>hibernate-jpamodelgen</artifactId>
    *   </dependency>
    *
    *   After that models will be generated on Maven stage "compile". So to generate it, you need to run
    *   maven with goals 'clean' and 'compile'.
    * */

    /**
     * This method returns {@link List} of all {@link Student} which have the column 'scholarship' with passed value.
     * IMPORTANT: this method has to be implemented using EntityManager and JPQL;
     *
     * @param scholarship a boolean value that indicates whether a student has a scholarship or does not have.
     */
    List<StudentIdAndNameDto> findByScholarshipUsingJPQL(Boolean scholarship);

    /**
     * This method returns {@link List} of all {@link Student} which have the column 'scholarship' with passed value.
     * IMPORTANT: this method has to be implemented using Criteria API (and Static Metamodel instead of String names of field);
     *
     * @param scholarship a boolean value that indicates whether a student has a scholarship or does not have.
     */
    List<StudentIdAndNameDto> findByScholarshipUsingCriteriaApi(Boolean scholarship);

}
