package org.example.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.Period;

@Entity
@Table(name = "persons")
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
@ToString(callSuper=true)
@DiscriminatorColumn(name = "dtype", discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue("person")
public abstract class Person extends BaseEntity {
    @NotBlank(message = "Name is required") private String name;
    @NotNull(message = "Birth date is required") @PastOrPresent(message = "Birth date must be in the past or present") @Column(name = "birth_date", nullable = false) private LocalDate birth_date;

    public int getAge() {
        return Period.between(birth_date, LocalDate.now()).getYears();
    }
}
