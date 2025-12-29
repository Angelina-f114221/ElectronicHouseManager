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
    private boolean has_pet_using_cа;

    @ManyToMany(mappedBy = "apartments")
    private Set<Owner> owners;

    @ManyToMany(mappedBy = "apartments")
    private Set<Resident> residents;

    @ManyToOne
    private Building building;
}
