package org.example.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDate;

@Entity
public class Resident extends BaseEntity {
    private String name;
    private int age;
    private boolean uses_elevator;
    private boolean pet_uses_common_areas;
    private LocalDate contract_start;
    private LocalDate last_payment;
    private boolean has_paid_monthly_fee;
    private boolean is_owner;
    // apartment_id INT
}
