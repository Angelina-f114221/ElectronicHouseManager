package org.example.entity;

import jakarta.persistence.*;
import lombok.ToString;

import java.util.Set;

@Entity
@ToString(callSuper=true)
public class Apartment extends BaseEntity {
    private int floor;
    private int number;
    private double area;

    @ManyToMany(mappedBy = "owner")
    private Set<Owner> owners;

    @ManyToMany(mappedBy = "resident")
    private Set<Resident> residents;

    @ManyToOne
    private Building building;
}
