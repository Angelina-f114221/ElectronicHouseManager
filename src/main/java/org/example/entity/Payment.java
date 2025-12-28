package org.example.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
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

    @ManyToOne
    private Company company;
    @ManyToOne
    private Employee employee;
    @ManyToOne
    private Building building;
    @ManyToOne
    private Apartment apartment;
}


