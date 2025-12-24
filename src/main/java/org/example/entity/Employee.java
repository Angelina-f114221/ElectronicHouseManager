package org.example.entity;

import jakarta.persistence.*;
import lombok.ToString;

import java.util.Set;

@Entity
@ToString(callSuper=true)
public class Employee extends BaseEntity {
    private String name;
    private int age;

    @ManyToOne
    private Company company;

    @OneToMany(mappedBy = "employee")
    private Set<Building> buildings;
}
