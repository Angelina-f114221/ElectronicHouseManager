package org.example.dto;

import lombok.*;

import java.time.LocalDate;
@AllArgsConstructor
@Getter
@Setter
@ToString
@NoArgsConstructor

public class PaymentDto {
    private long id;
    private double amount;
    private LocalDate payment_date;
    private String period;
    private long apartment_id;
}
