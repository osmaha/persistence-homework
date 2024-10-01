package com.verong.demo.database.custom.hibernate.persistence.model;

import com.verong.demo.database.custom.hibernate.persistence.orm.annotation.MyColumn;
import com.verong.demo.database.custom.hibernate.persistence.orm.annotation.MyEntity;
import com.verong.demo.database.custom.hibernate.persistence.orm.annotation.MyId;
import com.verong.demo.database.custom.hibernate.persistence.orm.annotation.MyTable;
import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Builder
@MyEntity
@MyTable(name = "students")
public class Student {
    @MyId
    private Long id;

    private String email;

    @MyColumn(name = "first_name")
    private String firstName;

    @MyColumn(name = "last_name")
    private String lastName;

    private LocalDate birthday;

    private Boolean scholarship;

    private StudentStatus status;
}
