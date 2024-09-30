package com.verong.demo.database.hibernate.persistence.dao;

import com.verong.demo.database.hibernate.persistence.dto.StudentIdAndNameDto;
import com.verong.demo.database.hibernate.persistence.model.Student;
import com.verong.demo.database.hibernate.persistence.model.StudentStatus;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.ParameterExpression;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

@RequiredArgsConstructor
public class StudentDaoImpl implements StudentDao {

    private final EntityManagerFactory emf;

    @Override
    public void create(Student student) {
        doInTransaction(em -> em.persist(student));
    }

    @Override
    public Optional<Student> findById(Long id) {
        var student = doInTransactionWithReturn(em -> em.find(Student.class, id));
        return Optional.ofNullable(student);
    }

    @Override
    public List<Student> findAll() {
        return doInTransactionWithReturn(em -> {
            var selectAllStudentQuery = em.createQuery("select s from Student s", Student.class);
            return selectAllStudentQuery.getResultList();
        });
    }

    @Override
    public List<StudentIdAndNameDto> findByScholarshipUsingJPQL(Boolean scholarship) {
        return doInTransactionWithReturn(em -> {
            var jpql = """
                    select new com.verong.demo.database.hibernate.persistence.dto.StudentIdAndNameDto(s.id, s.firstName, s.lastName)
                    from Student s
                    where s.scholarship = :scholarship
                    """;
            var selectByScholarshipQuery = em.createQuery(jpql, StudentIdAndNameDto.class);
            selectByScholarshipQuery.setParameter("scholarship", scholarship);
            return selectByScholarshipQuery.getResultList();
        });
    }

    @Override
    public List<StudentIdAndNameDto> findByScholarshipUsingCriteriaApi(Boolean scholarship) {
        return doInTransactionWithReturn(em -> {

            // Create CriteriaBuilder and CriteriaQuery with expected result type
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<StudentIdAndNameDto> criteriaQuery = cb.createQuery(StudentIdAndNameDto.class);

            // Create Root<Student> object which defines "from students" clause
            Root<Student> root = criteriaQuery.from(Student.class);

            /*
                Using method select() we define "select s.id, s.first_name, s.last_name" clause. However, our goal is to
                get DTO as a result object. Calling cb.construct() we can tell JPA (Hibernate) to convert each database
                row into StudentIdAndNameDto.class using record constructor:

                StudentIdAndNameDto(Long id, String firstName,String lastname)
            */
            criteriaQuery.select(cb.construct(
                    StudentIdAndNameDto.class,
                    root.get("id"), root.get("firstName"), root.get("lastName"))
            );

            // Define "where s.scholarship = :scholarship" clause with parameter ":scholarship" that we will set later
            ParameterExpression<Boolean> scholarshipParameter = cb.parameter(Boolean.class);
            criteriaQuery.where(cb.equal(root.get("scholarship"), scholarshipParameter));

            // Create TypedQuery<StudentIdAndNameDto> to be able to set the parameter and call getResultList()
            TypedQuery<StudentIdAndNameDto> selectByScholarshipQuery = em.createQuery(criteriaQuery);
            selectByScholarshipQuery.setParameter(scholarshipParameter, scholarship);

            return selectByScholarshipQuery.getResultList();
        });
    }

    @Override
    public void updateStatus(Long id, StudentStatus status) {
        Objects.requireNonNull(id, "Student ID cannot be null");
        Objects.requireNonNull(status, "Student status cannot be null");

        doInTransaction(em -> {
            var student = em.find(Student.class, id);
            student.setStatus(status);
        });
    }

    @Override
    public void remove(Student student) {
        Objects.requireNonNull(student, "Student cannot be null");
        var studentId = student.getId();
        Objects.requireNonNull(studentId, "Student ID cannot be null");

        doInTransaction(em -> {
            var reference = em.getReference(Student.class, studentId);
            em.remove(reference);
        });
    }


    /*
        The util method that is used to run queries without return value inside the one transaction.
        If we want to have simple transaction management, we can use the same approach of how to begin, commit and rollback
        a transaction.
    */
    private void doInTransaction(Consumer<EntityManager> worker) {
        var em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            worker.accept(em);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    /*
        The util method that is used to run queries with return type such as Student or List<Student> objects.
        The same explanation as we have one the method above.
    */
    private <T> T doInTransactionWithReturn(Function<EntityManager, T> worker) {
        var em = emf.createEntityManager();
        try {
            em.getTransaction().begin();

            T result = worker.apply(em);

            em.getTransaction().commit();
            return result;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }
}
