package org.example.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
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
    @Positive(message = "Apartment number must be >= 1") private int number;
    @Positive(message = "Floor must be >= 1") private int floor;
    @Positive(message = "Area must be > 0.0") private BigDecimal area;
    @Min(value = 0, message = "Pets must be >= 0") private int pets_using_ca;

    @ManyToMany(mappedBy = "apartments")
    // избягва се безкрайна рекурсия на извикване на тостринг-ове
    @ToString.Exclude
    private Set<Owner> owners;

    @OneToMany(mappedBy = "apartment")
    @ToString.Exclude
    private Set<Resident> residents;

    // данните за сградата се зареждат само ако пожелаем, но не и по подразбиране
    // @NotNull	- Java, optional = false - Hibernate (апартаментът трябва да има сграда), nullable = false - БД
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @NotNull
    @JoinColumn(name = "building_id", nullable = false)
    private Building building;

    // ако се изтрие апартамент, трием и всички плащания за него
    @OneToMany(mappedBy = "apartment", orphanRemoval = true)
    @ToString.Exclude
    private Set<Payment> payments;
}