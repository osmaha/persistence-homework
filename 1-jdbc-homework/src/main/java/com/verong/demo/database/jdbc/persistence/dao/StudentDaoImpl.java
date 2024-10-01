package com.verong.demo.database.jdbc.persistence.dao;

import com.verong.demo.database.jdbc.persistence.model.Student;
import com.verong.demo.database.jdbc.persistence.model.StudentStatus;
import lombok.RequiredArgsConstructor;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
public class StudentDaoImpl implements StudentDao {

    private static final String INSERT_STUDENT_SQL = """
            insert into students(email, first_name, last_name, birthday, scholarship, status)
            values(?, ?, ?, ?, ?, ?) returning id;
            """;

    private static final String SELECT_STUDENT_BY_ID_SQL = """
            select id, email, first_name, last_name, birthday, scholarship, status
            from students where id = ?;
            """;

    private static final String SELECT_ALL_STUDENTS_SQL = """
            select id, email, first_name, last_name, birthday, scholarship, status
            from students;
            """;

    private static final String UPDATE_STUDENT_SQL = """
            update students set email = ?, first_name = ?, last_name = ?, birthday = ?, scholarship = ?, status = ?
            where id = ?
            """;

    private static final String DELETE_STUDENT_BY_ID_SQL = """
            delete from students where id = ?;
            """;

    private final DataSource dataSource;

    @Override
    public void create(Student student) {
        Objects.requireNonNull(student, "Student is null");
        try (var connection = dataSource.getConnection()) {
            createStudent(student, connection);
        } catch (SQLException e) {
            throw new StudentDaoException("Error during saving student: %s".formatted(student), e);
        }
    }

    private void createStudent(Student student, Connection connection) throws SQLException {
        var insertStatement = connection.prepareStatement(INSERT_STUDENT_SQL, Statement.RETURN_GENERATED_KEYS);
        fillStudentStatement(student, insertStatement);
        insertStatement.executeUpdate();

        var studentId = fetchGeneratedId(insertStatement);
        student.setId(studentId);
    }

    private void fillStudentStatement(Student student, PreparedStatement studentStatement) throws SQLException {
        studentStatement.setString(1, student.getEmail());
        studentStatement.setString(2, student.getFirstName());
        studentStatement.setString(3, student.getLastName());
        studentStatement.setDate(4, Date.valueOf(student.getBirthday()));
        studentStatement.setBoolean(5, student.getScholarship());
        studentStatement.setString(6, student.getStatus().name());
    }

    private Long fetchGeneratedId(PreparedStatement insertStatement) throws SQLException {
        var generatedKeys = insertStatement.getGeneratedKeys();
        if (generatedKeys.next()) {
            return generatedKeys.getLong(1);
        } else {
            throw new StudentDaoException("Can not obtain generated student ID");
        }
    }

    @Override
    public Optional<Student> findById(Long id) {
        Objects.requireNonNull(id, "Student ID is null");
        try (var connection = dataSource.getConnection()) {
            return Optional.ofNullable(findStudentById(id, connection));
        } catch (SQLException e) {
            throw new StudentDaoException("Error during selecting student by id %d".formatted(id), e);
        }
    }

    private Student findStudentById(Long id, Connection connection) throws SQLException {
        var selectByIdStatement = prepareSelectStudentByIdStatement(id, connection);
        var resultSet = selectByIdStatement.executeQuery();
        return resultSet.next() ? parseStudentRow(resultSet) : null;
    }

    private PreparedStatement prepareSelectStudentByIdStatement(Long id, Connection connection) {
        try {
            var selectByIdStatement = connection.prepareStatement(SELECT_STUDENT_BY_ID_SQL);
            selectByIdStatement.setLong(1, id);
            return selectByIdStatement;
        } catch (SQLException e) {
            throw new StudentDaoException("Statement for selecting student by id %d cannot be prepared".formatted(id), e);
        }
    }

    @Override
    public List<Student> findAll() {
        try (var connection = dataSource.getConnection()) {
            return findAllStudents(connection);
        } catch (SQLException e) {
            throw new StudentDaoException("Error during selecting all students", e);
        }
    }

    private List<Student> findAllStudents(Connection connection) throws SQLException {
        var statement = connection.createStatement();
        var resultSet = statement.executeQuery(SELECT_ALL_STUDENTS_SQL);
        return parseStudentToList(resultSet);
    }

    @Override
    public void update(Student student) {
        Objects.requireNonNull(student, "Student is null");
        try (var connection = dataSource.getConnection()) {
            updateStudent(student, connection);
        } catch (SQLException e) {
            throw new StudentDaoException("Error during updating student: %s".formatted(student), e);
        }
    }

    private void updateStudent(Student student, Connection connection) throws SQLException {
        var studentId = student.getId();

        var updateStudentStatement = connection.prepareStatement(UPDATE_STUDENT_SQL);
        fillStudentStatement(student, updateStudentStatement);
        updateStudentStatement.setLong(7, studentId);

        executeUpdateStudentById(updateStudentStatement, studentId);
    }

    @Override
    public void remove(Student student) {
        Objects.requireNonNull(student, "Student is null");

        var studentId = student.getId();
        Objects.requireNonNull(studentId, "Student ID is null");

        try (var connection = dataSource.getConnection()) {
            removeStudentById(studentId, connection);
        } catch (SQLException e) {
            throw new StudentDaoException("Error during deleting student by id = %d".formatted(studentId), e);
        }
    }

    private void removeStudentById(Long id, Connection connection) throws SQLException {
        var deleteStudentByIdStatement = connection.prepareStatement(DELETE_STUDENT_BY_ID_SQL);
        deleteStudentByIdStatement.setLong(1, id);
        executeUpdateStudentById(deleteStudentByIdStatement, id);
    }

    private void executeUpdateStudentById(PreparedStatement updateStatement, Long studentId) throws SQLException {
        int rowsAffected = updateStatement.executeUpdate();
        if (rowsAffected == 0) {
            throw new StudentDaoException(String.format("Student with id = %d does not exist", studentId));
        }
    }

    private List<Student> parseStudentToList(ResultSet resultSet) throws SQLException {
        var students = new ArrayList<Student>();
        while (resultSet.next()) {
            students.add(parseStudentRow(resultSet));
        }
        return students;
    }

    private Student parseStudentRow(ResultSet resultSet) throws SQLException {
        var student = new Student();
        student.setId(resultSet.getLong("id"));
        student.setEmail(resultSet.getString("email"));
        student.setFirstName(resultSet.getString("first_name"));
        student.setLastName(resultSet.getString("last_name"));
        student.setBirthday(resultSet.getDate("birthday").toLocalDate());
        student.setScholarship(resultSet.getBoolean("scholarship"));
        student.setStatus(StudentStatus.valueOf(resultSet.getString("status")));
        return student;
    }
}
