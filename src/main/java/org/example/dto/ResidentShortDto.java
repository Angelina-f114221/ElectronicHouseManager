package org.example.dto;

import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
@Setter
@ToString
@NoArgsConstructor

public class ResidentShortDto {
    private long id;
    private String name;
    private LocalDate birth_date;
}
