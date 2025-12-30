package org.example.dto;

import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
@Setter
@ToString
@NoArgsConstructor

public class BuildingDto {
    private long id;
    private String name;
    private int floors;
    private String address;
    private double common_areas;
    private double total_areas;
    private LocalDate contract_start_date;
    private double fee_per_sqm;
    private double fee_per_pet_using_ca;
    private double fee_per_person_over_7_using_elevator;

    private long companyId;
    private long employeeId;
}
