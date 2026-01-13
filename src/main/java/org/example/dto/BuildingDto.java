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
    @Positive(message = "Floors must be > 0") @NotNull(message = "Floors are required") private int floors;
    @NotBlank(message = "Address is required") @Size(min = 5, max = 50, message = "Address must be 5-50 characters Long") private String address;
    @NotNull(message = "Common area size is required") @DecimalMin(value = "0.1", message = "Common area size must be > 0.1") private BigDecimal common_areas;
    @NotNull(message = "Total area size is required") @DecimalMin(value = "0.1", message = "Total area size must be > 0.1") private BigDecimal total_areas;
    @NotNull(message = "Contract date is required") @PastOrPresent(message = "Birth date must be in the present or in the past") private LocalDate contract_start_date;
    @NotNull(message = "Company id is required") @Positive(message = "Company id must be > 0") private long company_id;
    private long employee_id;
}