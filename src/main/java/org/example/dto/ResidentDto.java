package org.example.dto;

import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
@Setter
@ToString
@NoArgsConstructor

public class ResidentDto {
    private long id;
    private String name;
    private int age;
    private boolean uses_elevator;
    private LocalDate contract_start;
    private long apartmentId;
}
