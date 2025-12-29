package org.example.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "residents")
@Getter
@Setter
@ToString(callSuper=true)
public class Resident extends BaseEntity {
    private String name;
    private int age;
    private boolean uses_elevator;
    private LocalDate contract_start;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "apartment_id")
    @ToString.Exclude
    private Apartment apartment;
}
