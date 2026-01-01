package org.example.dto;

import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor

public class PayRequestDto {
    private long apartmentId;
    private double amount;
    private LocalDate paymentDate;
}
