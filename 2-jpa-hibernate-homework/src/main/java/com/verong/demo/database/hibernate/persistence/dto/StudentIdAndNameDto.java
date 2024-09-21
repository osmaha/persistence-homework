package com.verong.demo.database.hibernate.persistence.dto;

public record StudentIdAndNameDto(
        Long id,
        String firstName,
        String lastname
) {
}
