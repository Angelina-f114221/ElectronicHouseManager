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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "building_id")
    @ToString.Exclude
    private Building building;

    @OneToMany(mappedBy = "apartment")
    @ToString.Exclude
    private Set<Payment> payments;
}
