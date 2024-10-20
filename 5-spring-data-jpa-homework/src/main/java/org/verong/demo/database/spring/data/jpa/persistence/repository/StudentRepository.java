package org.verong.demo.database.spring.data.jpa.persistence.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.verong.demo.database.spring.data.jpa.persistence.dto.StudentIdAndNameDto;
import org.verong.demo.database.spring.data.jpa.persistence.model.Student;
import org.verong.demo.database.spring.data.jpa.persistence.model.StudentStatus;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {

    /*
    *   This repository should extend JpaRepository. It will make it possible to create queries to database
    *   by adding methods with specific names.
    *
    *   Here you can find documentation with possible tokens:
    *   https://docs.spring.io/spring-data/jpa/reference/jpa/query-methods.html
    * */

    List<Student> findByLastName(String lastName);

    List<Student> findByStatusIn(List<StudentStatus> statuses);

    @Query("select s from Student s")
    List<Student> findAllAndSort(Sort sort);

    @Query("select s from Student s")
    Page<Student> findAndSort(Pageable pageable);

    String SELECT_BY_SCHOLARSHIP_JPQL = """
            select new org.verong.demo.database.spring.data.jpa.persistence.dto.StudentIdAndNameDto(s.id, s.firstName, s.lastName)
            from Student s
            where s.scholarship = :scholarship
            """;

    @Query(SELECT_BY_SCHOLARSHIP_JPQL)
    List<StudentIdAndNameDto> findByScholarshipUsingJPQL(@Param("scholarship") Boolean scholarship);

    <T> Optional<T> findById(Long id, Class<T> dtoType);

}
