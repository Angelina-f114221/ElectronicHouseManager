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
public class Employee extends Person {

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @JoinColumn(name = "company_id")
    private Company company;

    @OneToMany(mappedBy = "employee")
    @ToString.Exclude
    private Set<Building> buildings;
}
