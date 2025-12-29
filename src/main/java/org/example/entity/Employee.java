package org.example.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Set;

@Entity
@Getter
@Setter
@ToString(callSuper=true)
public class Employee extends BaseEntity {
    private String name;
    private int age;

    @ManyToOne(fetch = FetchType.LAZY)
    private Company company;

    @OneToMany(mappedBy = "employee")
    @ToString.Exclude
    private Set<Building> buildings;
}
