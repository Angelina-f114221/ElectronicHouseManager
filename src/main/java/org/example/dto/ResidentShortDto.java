package org.example.dto;

import lombok.*;

@AllArgsConstructor
@Getter
@Setter
@ToString
@NoArgsConstructor

public class ResidentShortDto {
    private long id;
    private String name;
    private int age;
}
