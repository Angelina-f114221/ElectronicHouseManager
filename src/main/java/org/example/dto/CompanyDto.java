package org.example.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@AllArgsConstructor
@Getter
@Setter
@ToString
@NoArgsConstructor

public class CompanyDto {
    private long id;
    @NotBlank(message = "Company name is required")
    @Size(min = 2, max = 30)
    private String name;
}
