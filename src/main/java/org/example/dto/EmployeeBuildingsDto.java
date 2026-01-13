package org.example.dto;

import lombok.*;

@AllArgsConstructor
@Getter
@Setter
@ToString
@NoArgsConstructor
// супер за филтриране и сортиране на служители и сградите им
public class EmployeeBuildingsDto {
    private long employee_id;
    private String employee_name;
    private long buildings_count;
}