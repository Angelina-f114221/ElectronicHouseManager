package org.example.entity;

import jakarta.persistence.*;
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
    private BigDecimal amount;
    private LocalDate payment_date;
    private String period;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @ToString.Exclude
    // без апартамент не може да има плащане
    @JoinColumn(name = "apartment_id", nullable = false)
    private Apartment apartment;
}


