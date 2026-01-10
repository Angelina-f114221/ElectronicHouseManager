package org.example.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Set;

@Entity
@Table(name = "owners")
@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper=true)
@DiscriminatorValue("Owner")
public class Owner extends Person {
    @ManyToMany
    @JoinTable(name = "owner_apartment")
    @ToString.Exclude
    private Set<Apartment> apartments;

}
