package org.example.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import java.time.LocalDate;
import java.time.Period;

@Entity
@Table(name = "persons")
// при тази стратегия и person си има основните данни, но наследниците си имат своите собствени. тоест не е всичко в една обща таблица
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
@ToString(callSuper=true)
@DiscriminatorColumn(name = "dtype", discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue("person")
@NoArgsConstructor
public abstract class Person extends BaseEntity {
    @NotBlank(message = "Name is required") @Size(min = 1, max = 50, message = "Name must be 1-50 characters") private String name;
    @NotNull(message = "Birth date is required") @PastOrPresent(message = "Birth date must be in the past or present") @Column(name = "birth_date", nullable = false) private LocalDate birth_date;
    // изчислява годините чрез вадене на рожденната дата от текущата
    public int getAge() {
        return Period.between(birth_date, LocalDate.now()).getYears();
    }
}