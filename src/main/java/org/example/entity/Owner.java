package org.example.entity;

import jakarta.persistence.*;

import java.util.Set;

@Entity
public class Owner extends BaseEntity {
    private String name;
    // ???
    @ManyToMany
    private Set<Apartment> apartments;

    // resident_id INT
}
