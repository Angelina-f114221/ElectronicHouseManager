package org.example.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
@Setter
@ToString
@NoArgsConstructor

public class ResidentDto {
    private long id;
    @NotBlank(message = "Name is required") @Size(min = 1, max = 50, message = "Name must be 1-50 characters") private String name;
    @NotNull(message = "Birth date is required") @PastOrPresent(message = "Birth date must be in the past or present") private LocalDate birth_date;
    private boolean uses_elevator;
    @NotNull(message = "Contract start date is required") @PastOrPresent(message = "Contract start date must be in the past or present") private LocalDate contract_start;
    @Positive(message = "Apartment id must be > 0") @NotNull(message = "Apartment id is required") private long apartment_id;
}