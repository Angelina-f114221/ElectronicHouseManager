package org.example.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Apartment extends BaseEntity {
    private int floor;
    private int number;
    private double area;
    // owner_id INT
    // building_id INT
}
