package org.example.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;
import java.util.Set;

@AllArgsConstructor
@Getter
@Setter
@ToString
@NoArgsConstructor

public class OwnerDto {
    private long id;
    @NotBlank(message = "Name is required") @Size(min = 2, max = 30, message = "Name must be 2-30 characters") private String name;
    @NotNull(message = "Birth date is required") @PastOrPresent(message = "Birth date must be in the past or present") private LocalDate birth_date;
    private Set<Long> apartment_ids;
}