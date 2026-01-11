package org.example.entity;

import jakarta.persistence.*;
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

    @Column(nullable = false) private BigDecimal fee_per_sqm;
    @Column(nullable = false) private BigDecimal fee_per_pet_using_ca;
    @Column(nullable = false) private BigDecimal fee_per_person_over_7_using_elevator;
    @Column(nullable = false) private LocalDate start_date;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "building_id", nullable = false)
    private Building building;

}
