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
    @Positive
    private BigDecimal amount;
    @NotNull
    @PastOrPresent
    private LocalDate payment_date;
    @NotBlank
    @Size(min = 3, max = 20)
    private String period;
    @Positive
    @NotNull
    private long apartment_id;
}
