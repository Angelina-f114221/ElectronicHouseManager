package org.example.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Building {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private int floors;
    private String address;
    private double common_areas;
    private double total_areas;
    private int n_apartments;
    private double fee_per_sqm;
    private double fee_per_pet_using_ca;
    // employee_id INT
}
