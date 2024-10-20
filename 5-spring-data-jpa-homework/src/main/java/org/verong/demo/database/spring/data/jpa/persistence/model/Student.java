package org.verong.demo.database.spring.data.jpa.persistence.model;

import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Builder
public class Student {

    private Long id;

    private String email;

    private String firstName;

    private String lastName;

    private LocalDate birthday;

    private Boolean scholarship;

    private StudentStatus status;
}
