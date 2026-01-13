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
// използва се в payment service. не се включват amount и period, защото се изчисляват
public class PayRequestDto {
    @Positive(message = "Apartment id must be > 0") @NotNull(message = "Apartment id is required") private long apartment_id;
    @NotNull(message = "Payment date is required") @PastOrPresent(message = "Payment date must be in the past or present") private LocalDate payment_date;
}