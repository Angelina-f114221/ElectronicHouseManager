package org.example.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString

public class ApartmentDto {
    private long id;
    @Positive(message = "Apartment number must be >= 1") private int number;
    @Positive(message = "Floor must be >= 1") private int floor;
    @Positive(message = "Area must be > 0.0") private BigDecimal area;
    @Min(value = 0, message = "Pets must be >= 0") private int pets_using_ca;
    @Positive(message = "Building id must be > 0") private long building_id;
}
