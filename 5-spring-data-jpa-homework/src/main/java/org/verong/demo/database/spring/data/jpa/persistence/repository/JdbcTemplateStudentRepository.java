package org.verong.demo.database.spring.data.jpa.persistence.repository;

import org.verong.demo.database.spring.data.jpa.persistence.dto.StudentIdAndNameDto;
import org.verong.demo.database.spring.data.jpa.persistence.model.Student;

import java.util.List;

public interface JdbcTemplateStudentRepository {

    /**
     * This method returns {@link List} of all {@link Student} which have the column 'scholarship' with passed value.
     * IMPORTANT: this method has to be implemented using JdbcTemplate and RowMapper;
     *
     * @param scholarship a boolean value that indicates whether a student has a scholarship or does not have.
     */
    List<StudentIdAndNameDto> findByScholarshipUsingSQL(Boolean scholarship);

}
