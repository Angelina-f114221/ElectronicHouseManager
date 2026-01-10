package org.example.dto;

import lombok.*;

@AllArgsConstructor
@Getter
@Setter
@ToString
@NoArgsConstructor

public class EmployeeBuildingsDto {
    private long employee_id;
    private String employee_name;
    private long buildings_count;
}
