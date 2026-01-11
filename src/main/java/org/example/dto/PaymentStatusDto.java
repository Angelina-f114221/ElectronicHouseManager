package org.example.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@AllArgsConstructor
@Getter
@Setter
@ToString
@NoArgsConstructor
public class PaymentStatusDto {
    private long id;
    @NotBlank(message = "Code is required") @Size(min = 1, max = 50, message = "Code must be 1-50 characters") private String code;
    @Size(min = 1, max = 255, message = "Description must be 1-255 characters") private String description;
}
