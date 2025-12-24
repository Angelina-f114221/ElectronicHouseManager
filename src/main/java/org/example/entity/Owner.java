package org.example.entity;

import jakarta.persistence.*;
import lombok.ToString;

import java.util.Set;

@Entity
@ToString(callSuper=true)
public class Owner extends BaseEntity {
    private String name;
    // ???
    @ManyToMany
    private Set<Apartment> apartments;

    // resident_id INT
}
