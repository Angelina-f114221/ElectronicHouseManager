package org.example.dto;

import lombok.*;

@AllArgsConstructor
@Getter
@Setter
@ToString
@NoArgsConstructor

public class CompanyIncomeDto {
    private long companyId;
    private String companyName;
    private double income;   // SUM(p.amount)
}
