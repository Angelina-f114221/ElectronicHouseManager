package org.example.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
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
// помага да различим типа човек в таблицата person
@DiscriminatorValue("Employee")
public class Employee extends Person {
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "employee_company")
    @NotEmpty(message = "Employee must have at least one company")
    @ToString.Exclude
    private Set<Company> companies;

    @OneToMany(mappedBy = "employee")
    @ToString.Exclude
    private Set<Building> buildings;
}