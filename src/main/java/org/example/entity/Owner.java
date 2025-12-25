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
public class Owner extends BaseEntity {
    private String name;
    // ???
    @ManyToMany
    private Set<Apartment> apartments;

    // resident_id INT
}
