package org.verong.demo.database.spring.data.jpa.persistence.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Builder
@Entity
@Table(name = "students")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "students-id-generator")
    @SequenceGenerator(name = "students-id-generator", sequenceName = "students_id_seq", allocationSize = 1)
    private Long id;

    private String email;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    private LocalDate birthday;

    private Boolean scholarship;

    @Enumerated(EnumType.STRING)
    private StudentStatus status;
}
