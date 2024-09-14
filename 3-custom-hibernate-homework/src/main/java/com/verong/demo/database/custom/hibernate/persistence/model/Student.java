package com.verong.demo.database.custom.hibernate.persistence.model;

import com.verong.demo.database.custom.hibernate.persistence.orm.annotation.MyEntity;
import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Builder
@MyEntity
public class Student {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private LocalDate birthday;
    private Boolean scholarship;
    private StudentStatus status;
}
