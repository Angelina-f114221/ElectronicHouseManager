package org.example.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
@Setter
@ToString
@NoArgsConstructor

public class BuildingDto {
    private long id;
    @NotBlank
    @Size(min = 2, max = 30)
    private String name;
    @Min(1)
    private int floors;
    @NotBlank
    @Size(min = 5, max = 50)
    private String address;
    @PositiveOrZero private double common_areas;
    @PositiveOrZero private double total_areas;
    @PastOrPresent
    private LocalDate contract_start_date;
    @PositiveOrZero private double fee_per_sqm;
    @PositiveOrZero private double fee_per_pet_using_ca;
    @PositiveOrZero private double fee_per_person_over_7_using_elevator;

    private Long company_id;
    @NotNull
    @Positive
    private Long employee_id;
}
