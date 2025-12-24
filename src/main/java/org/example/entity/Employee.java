package org.example.entity;

import jakarta.persistence.*;

import java.util.Set;

@Entity
public class Employee extends BaseEntity {
    private String name;
    private int age;

    @ManyToOne
    private Company company;

    @OneToMany(mappedBy = "employee")
    private Set<Building> buildings;
}
