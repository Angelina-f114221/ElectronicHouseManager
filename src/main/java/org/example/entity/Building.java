package org.example.entity;

import jakarta.persistence.*;

import java.util.Set;

@Entity
public class Building extends BaseEntity {
    private String name;
    private int floors;
    private String address;
    private double common_areas;
    private double total_areas;
    private int n_apartments;
    private double fee_per_sqm;
    private double fee_per_pet_using_ca;
    // employee_id INT
    @OneToMany(mappedBy = "building")
    private Set<Apartment> apartments;

    @ManyToOne
    private Company company;

    @ManyToOne
    private Employee employee;
}
