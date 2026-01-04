package org.example.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "buildings")
@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper=true)
public class Building extends BaseEntity {
    private String name;
    private int floors;
    private String address;
    private BigDecimal common_areas;
    private BigDecimal total_areas;
    private LocalDate contract_start_date;
    private BigDecimal fee_per_sqm;
    private BigDecimal fee_per_pet_using_ca;
    private BigDecimal fee_per_person_over_7_using_elevator;

    @OneToMany(mappedBy = "building")
    @ToString.Exclude
    private Set<Apartment> apartments;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @JoinColumn(name = "company_id")
    private Company company;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @JoinColumn(name = "employee_id")
    private Employee employee;
}
