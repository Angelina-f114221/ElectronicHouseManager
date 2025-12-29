package org.example.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Set;

@Entity
@Table(name = "owners")
@Getter
@Setter
@ToString(callSuper=true)
public class Owner extends BaseEntity {
    private String name;
    @ManyToMany
    @JoinTable(name = "owner_apartment")
    @ToString.Exclude
    private Set<Apartment> apartments;

}
