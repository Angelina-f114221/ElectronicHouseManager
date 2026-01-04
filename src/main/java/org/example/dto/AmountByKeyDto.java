package org.example.dto;

import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@Getter
@Setter
@ToString
@NoArgsConstructor

public class AmountByKeyDto {
    private long id;
    private String name;
    private BigDecimal amount;

    public AmountByKeyDto(long id, String name, Number amount) {
        this(id, name, amount != null ? BigDecimal.valueOf(amount.doubleValue()) : BigDecimal.ZERO);
    }
}


