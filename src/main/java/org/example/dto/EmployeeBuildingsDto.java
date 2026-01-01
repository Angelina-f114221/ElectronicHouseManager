package org.example.dto;

import lombok.*;

@AllArgsConstructor
@Getter
@Setter
@ToString
@NoArgsConstructor

public class EmployeeBuildingsDto {
    private long employeeId;
    private String employeeName;
    private long buildingsCount;
}
