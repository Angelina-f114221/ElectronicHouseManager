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

public class BuildingDto {
    private long id;
    @NotBlank(message = "Building name is required") @Size(min = 2, max = 30, message =  "Building name must be 2-30 characters") private String name;
    @Positive(message = "Floors must be > 0") private int floors;
    @NotBlank(message = "Address is required") @Size(min = 5, max = 50, message = "Address must be 5-50 characters long") private String address;
    @DecimalMin(value = "0.1", message = "Amount must be > 0.1") private BigDecimal common_areas;
    @DecimalMin(value = "0.1", message = "Amount must be > 0.1") private BigDecimal total_areas;
    @NotNull(message = "Contract date is required") @PastOrPresent(message = "Birth date must be in the present or in the past") private LocalDate contract_start_date;
    @DecimalMin(value = "0.1", message = "Amount must be > 0.1") private BigDecimal fee_per_sqm;
    @DecimalMin(value = "0.1", message = "Amount must be > 0.1") private BigDecimal fee_per_pet_using_ca;
    @DecimalMin(value = "0.1", message = "Amount must be > 0.1") private BigDecimal fee_per_person_over_7_using_elevator;
    @Positive(message = "Company id must be > 0") private Long company_id;
    @Positive(message = "Employee id must be > 0") private Long employee_id;
}
