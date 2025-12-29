package org.example.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Set;

@Entity
@Table(name = "apartments")
@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper=true)
public class Apartment extends BaseEntity {
    private int floor;
    private int number;
    private double area;
    private int pets_using_ca;

    @ManyToMany(mappedBy = "apartments")
    @ToString.Exclude
    private Set<Owner> owners;

    @OneToMany(mappedBy = "apartment")
    @ToString.Exclude
    private Set<Resident> residents;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "building_id", nullable = false)
    @ToString.Exclude
    private Building building;

    @OneToMany(mappedBy = "apartment", orphanRemoval = true)
    @ToString.Exclude
    private Set<Payment> payments;
}
