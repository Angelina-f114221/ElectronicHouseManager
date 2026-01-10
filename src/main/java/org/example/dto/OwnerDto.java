package org.example.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
@Setter
@ToString
@NoArgsConstructor

public class OwnerDto {
    private long id;
    @NotBlank
    @Size(min = 2, max = 30)
    private String name;
    @NotNull
    @PastOrPresent
    private LocalDate birth_date;
}
