package org.example.entity;

import jakarta.persistence.*;
import lombok.ToString;

import java.time.LocalDate;
import java.util.Set;

@Entity
@ToString(callSuper=true)
public class Resident extends BaseEntity {
    private String name;
    private int age;
    private boolean uses_elevator;
    private boolean pet_uses_common_areas;
    private LocalDate contract_start;
    private LocalDate last_payment;
    private boolean has_paid_monthly_fee;
    private boolean is_owner;

    @ManyToMany
    private Set<Apartment> apartments;
}
