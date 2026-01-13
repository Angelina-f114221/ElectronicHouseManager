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
public class FeeDto {
    private long id;

    @NotNull(message = "Fee per sqm is required") @Positive(message = "Fee per sqm must be > 0") private BigDecimal fee_per_sqm;
    @NotNull(message = "Fee per pet is required") @Positive(message = "Fee per pet must be > 0") private BigDecimal fee_per_pet_using_ca;
    @NotNull(message = "Fee per person using elevator is required") @Positive(message = "Fee per person must be > 0") private BigDecimal fee_per_person_over_7_using_elevator;
    @NotNull(message = "Start date is required") @PastOrPresent(message = "Start date must be in the past or present") private LocalDate start_date;
    @Positive(message = "Building id must be > 0") @NotNull(message = "Building id is required") private long building_id;
}