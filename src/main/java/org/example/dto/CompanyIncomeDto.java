package org.example.dto;

import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@Getter
@Setter
@ToString
@NoArgsConstructor

public class CompanyIncomeDto {
    private long companyId;
    private String companyName;
    private BigDecimal income;

    public CompanyIncomeDto(long id, String name, Number income) {
        this(id, name, income != null ? BigDecimal.valueOf(income.doubleValue()) : BigDecimal.ZERO);
    }
}
