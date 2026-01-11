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

    @OneToMany(mappedBy = "building")
    @ToString.Exclude
    private Set<Apartment> apartments;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @OneToMany(mappedBy = "building", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Fee> fees;
}
