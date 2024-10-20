package org.verong.demo.database.spring.data.jpa.persistence.dto;

public record StudentIdAndNameDto(
        Long id,
        String firstName,
        String lastname
) {
}
