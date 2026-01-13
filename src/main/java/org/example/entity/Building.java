package org.example.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "buildings")
@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper=true)
public class Building extends BaseEntity {
    @NotBlank(message = "Building name is required") @Size(min = 2, max = 30, message =  "Building name must be 2-30 characters") private String name;
    @Positive(message = "Floors must be > 0") private int floors;
    @NotBlank(message = "Address is required") @Size(min = 5, max = 50, message = "Address must be 5-50 characters long") private String address;
    @DecimalMin(value = "0.1", message = "Amount must be > 0.1") private BigDecimal common_areas;
    @DecimalMin(value = "0.1", message = "Amount must be > 0.1") private BigDecimal total_areas;
    @NotNull(message = "Contract date is required") @PastOrPresent(message = "Birth date must be in the present or in the past") private LocalDate contract_start_date;

    @OneToMany(mappedBy = "building")
    @ToString.Exclude
    private Set<Apartment> apartments;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    private Employee employee;
    // ако се изтрие building, се трият и fees
    @OneToMany(mappedBy = "building", orphanRemoval = true)
    private Set<Fee> fees;
}