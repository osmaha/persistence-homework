package org.verong.demo.database.spring.data.jpa.persistence.repository.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.verong.demo.database.spring.data.jpa.persistence.dto.StudentIdAndNameDto;
import org.verong.demo.database.spring.data.jpa.persistence.repository.JdbcTemplateStudentRepository;

import java.util.List;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class JdbcTemplateStudentRepositoryImpl implements JdbcTemplateStudentRepository {

    private static final String ID_COLUMN = "id";
    private static final String FIRST_NAME_COLUMN = "first_name";
    private static final String LAST_NAME_COLUMN = "last_name";

    private static final String SELECT_BY_SCHOLARSHIP_PREPARED_SQL_TEMPLATE = """
            select s.%s, s.%s, s.%s from students s where s.scholarship = ?;
            """;

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<StudentIdAndNameDto> findByScholarshipUsingSQL(Boolean scholarship) {
        var sql = SELECT_BY_SCHOLARSHIP_PREPARED_SQL_TEMPLATE.formatted(ID_COLUMN, FIRST_NAME_COLUMN, LAST_NAME_COLUMN);

        RowMapper<StudentIdAndNameDto> rawMapper =
                (rs, rowNum) -> new StudentIdAndNameDto(rs.getLong(ID_COLUMN), rs.getString(FIRST_NAME_COLUMN), rs.getString(LAST_NAME_COLUMN));

        return jdbcTemplate.query(sql, rawMapper, scholarship);
    }
}
