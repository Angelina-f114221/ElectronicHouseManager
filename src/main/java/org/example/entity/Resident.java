package org.example.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import java.time.LocalDate;

@Entity
@Table(name = "residents")
@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper=true)
@DiscriminatorValue("Resident")
public class Resident extends Person {
    private boolean uses_elevator;
    @NotNull(message = "Contract start date is required") @PastOrPresent(message = "Contract start date must be in the past or present") private LocalDate contract_start;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @NotNull(message = "Apartment is required")
    @JoinColumn(name = "apartment_id", nullable = false)
    private Apartment apartment;
}