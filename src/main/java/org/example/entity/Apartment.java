package org.example.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
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
    private BigDecimal area;
    private int pets_using_ca;

    @ManyToMany(mappedBy = "apartments")
    // избягва се безкрайна рекурсия на извикване на тостринг-ове
    @ToString.Exclude
    private Set<Owner> owners;

    @OneToMany(mappedBy = "apartment")
    @ToString.Exclude
    private Set<Resident> residents;

    // данните за сградата се зареждат само ако пожелаем, но не и по подразбиране
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "building_id", nullable = false)
    @ToString.Exclude
    private Building building;

    // ако се изтрие апартамент, трием и всички плащания за него
    @OneToMany(mappedBy = "apartment", orphanRemoval = true)
    @ToString.Exclude
    private Set<Payment> payments;
}
