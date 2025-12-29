package org.example.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "residents")
@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper=true)
public class Resident extends Person {
    private boolean uses_elevator;
    private LocalDate contract_start;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "apartment_id", nullable = false)
    @ToString.Exclude
    private Apartment apartment;
}
