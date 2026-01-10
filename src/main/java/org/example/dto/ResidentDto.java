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
    @NotBlank(message = "Name is required") private String name;
    @NotNull(message = "Birth date is required") @PastOrPresent(message = "Birth date must be in the past or present") private LocalDate birth_date;
    private boolean uses_elevator;
    @NotNull(message = "Contract start date is required") @PastOrPresent(message = "Contract start date must be in the past or present") private LocalDate contract_start;
    @Positive(message = "Apartment id must be > 0") private long apartment_id;
}
