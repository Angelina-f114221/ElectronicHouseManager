package org.example.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "payments")
@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper=true)
public class Payment extends BaseEntity {
    @Positive(message = "Amount must be > 0") private BigDecimal amount;
    @NotNull(message = "Payment date is required") @PastOrPresent(message = "Payment date must be in the past or present") private LocalDate payment_date;
    @NotBlank(message = "Period is required") @Size(min = 3, max = 20, message = "Period must be 3-20 characters") private String period;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    // без апартамент не може да има плащане
    @NotNull
    @JoinColumn(name = "apartment_id", nullable = false)
    private Apartment apartment;

    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    @JoinColumn(name = "status_id")
    private PaymentStatus status;
}