package org.example.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Set;

@Entity
@Table(name = "employees")
@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper=true)
@DiscriminatorValue("Employee")
public class Employee extends Person {

    @ManyToMany
    @JoinTable(name = "employee_company")
    @ToString.Exclude
    private Set<Company> companies;

    @OneToMany(mappedBy = "employee")
    @ToString.Exclude
    private Set<Building> buildings;
}
