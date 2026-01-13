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
    private BigDecimal amount;
    private LocalDate payment_date;
    private String period;
    @Positive(message = "Apartment id must be > 0") @NotNull(message = "Apartment id is required") private long apartment_id;
    private String status_code;
}