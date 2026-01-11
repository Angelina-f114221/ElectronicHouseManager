package org.example.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "fees")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "building")
public class Fee extends BaseEntity {

    @NotNull(message = "Fee per sqm is required") @Positive(message = "Fee per sqm must be > 0") private BigDecimal fee_per_sqm;
    @NotNull(message = "Fee per pet is required") @Positive(message = "Fee per pet must be > 0") private BigDecimal fee_per_pet_using_ca;
    @NotNull(message = "Fee per person using elevator is required") @Positive(message = "Fee per person must be > 0") private BigDecimal fee_per_person_over_7_using_elevator;
    @NotNull(message = "Start date is required") private LocalDate start_date;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @Positive(message = "Building id must be > 0")
    @JoinColumn(name = "building_id", nullable = false)
    private Building building;

}
