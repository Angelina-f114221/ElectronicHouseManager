package org.example.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString

public class ApartmentDto {
    private long id;
    private int number;
    private int floor;
    private double area;
    private int pets_using_ca;
    private long building_id;
    private long ownerId;
}
