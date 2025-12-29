package org.example.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "buildings")
@Getter
@Setter
@ToString(callSuper=true)
public class Building extends BaseEntity {
    private String name;
    private int floors;
    private String address;
    private double common_areas;
    private double total_areas;
    private LocalDate contract_start_date;
    private double fee_per_sqm;
    private double fee_per_pet_using_ca;
    private double fee_per_person_over_7_using_elevator;

    @OneToMany(mappedBy = "building")
    @ToString.Exclude
    private Set<Apartment> apartments;

    @ManyToOne(fetch = FetchType.LAZY)
    private Company company;

    @ManyToOne(fetch = FetchType.LAZY)
    private Employee employee;
}
