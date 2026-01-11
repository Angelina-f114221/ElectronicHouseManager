package org.example.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "payment_status")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class PaymentStatus extends BaseEntity {
    @Column(unique = true, nullable = false)
    private String code;
    private String description;
}
