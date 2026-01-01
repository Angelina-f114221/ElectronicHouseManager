package org.example.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
@AllArgsConstructor
@Getter
@Setter
@ToString
@NoArgsConstructor

public class PaymentDto {
    private long id;
    @Positive
    private double amount;
    @NotNull
    @PastOrPresent
    private LocalDate payment_date;
    @NotBlank
    @Size(min = 3, max = 20)
    private String period;
    @Positive
    private long apartment_id;
}
