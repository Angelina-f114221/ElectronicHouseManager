package org.example.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Apartment {
    @Id
    private long id;
    private int floor;
    private int number;
    private double area;
    // owner_id INT
    // building_id INT
}
