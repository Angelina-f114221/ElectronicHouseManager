package org.example.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString

public class ApartmentDto {
    private long id;
    @NotNull(message = "Apartment number is required") @Positive(message = "Apartment number must be >= 1") private int number;
    @NotNull(message = "Floor is required") @Positive(message = "Floor must be >= 1") private int floor;
    @NotNull(message = "Area size is required") @Positive(message = "Area size must be > 0.0") private BigDecimal area;
    @NotNull(message = "Pets count using common area is required") @Min(value = 0, message = "Pets count using common area must be >= 0") private int pets_using_ca;
    @NotNull(message = "Building id is required") @Positive(message = "Building id must be > 0") private long building_id;
}