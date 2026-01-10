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
    @Positive(message = "Apartment id must be > 0") private long apartment_id;
    @NotNull(message = "Payment date is required") @PastOrPresent(message = "Payment date must be in the past or present") private LocalDate payment_date;
}
