package org.example.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Table(name = "payment_status")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class PaymentStatus extends BaseEntity {
    @Column(unique = true, nullable = false)
    @NotBlank(message = "Code is required") @Size(min = 1, max = 50, message = "Code must be 1-50 characters") private String code;
    @Size(min = 1, max = 255, message = "Description must be 1-255 characters") private String description;

}
