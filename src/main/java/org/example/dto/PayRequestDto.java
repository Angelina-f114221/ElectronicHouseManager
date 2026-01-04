package org.example.dto;

import lombok.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor

public class PayRequestDto {
    @Positive
    private long apartment_id;
    @NotNull
    @PastOrPresent
    private LocalDate payment_date;
}
