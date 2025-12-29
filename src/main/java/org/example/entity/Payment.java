package org.example.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@ToString(callSuper=true)
public class Payment extends BaseEntity {
    private double amount;
    private LocalDate payment_date;
    private String period;

    @ManyToOne(fetch = FetchType.LAZY)
    private Apartment apartment;
}


