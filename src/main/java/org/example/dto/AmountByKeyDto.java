package org.example.dto;

import lombok.*;

@AllArgsConstructor
@Getter
@Setter
@ToString
@NoArgsConstructor

public class AmountByKeyDto {
    private long id;
    private String name;
    private double amount;
}
