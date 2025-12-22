package org.example.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Owner extends BaseEntity {
    private String name;
    // apartment_id INT
    // resident_id INT
}
