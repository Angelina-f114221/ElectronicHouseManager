package org.example.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

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
    @Min(0)
    private int age;
}
