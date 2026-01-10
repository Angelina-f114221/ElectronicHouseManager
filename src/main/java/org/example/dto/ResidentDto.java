package org.example.dto;

import lombok.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;
import java.time.Period;

@AllArgsConstructor
@Getter
@Setter
@ToString
@NoArgsConstructor

public class ResidentDto {
    private long id;
    @NotBlank
    private String name;
    @NotNull
    @PastOrPresent
    private LocalDate birth_date;
    @NotBlank
    private boolean uses_elevator;
    @NotNull
    @PastOrPresent
    private LocalDate contract_start;
    @Positive
    @NotNull
    private long apartment_id;
}
