package org.example.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
@AllArgsConstructor
@Getter
@Setter
@ToString
@NoArgsConstructor

public class PaymentDto {
    private long id;
    @Positive(message = "Amount must be > 0") private BigDecimal amount;
    @NotNull(message = "Payment date is required") @PastOrPresent(message = "Payment date must be in the past or present") private LocalDate payment_date;
    @NotBlank(message = "Period is required") @Size(min = 3, max = 20, message = "Period must be 3-20 characters") private String period;
    @Positive(message = "Apartment id must be > 0") private long apartment_id;
}
