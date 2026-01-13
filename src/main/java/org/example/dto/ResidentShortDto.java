package org.example.dto;

import lombok.*;
import java.time.LocalDate;

@AllArgsConstructor
@Getter
@Setter
@ToString
@NoArgsConstructor
// съкратена версия за списък с живущи
//  няма валидация, защото вече е валидирано и е минало през ResidentDto
public class ResidentShortDto {
    private long id;
    private String name;
    private LocalDate birth_date;
}