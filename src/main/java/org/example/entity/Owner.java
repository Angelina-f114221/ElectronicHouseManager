package org.example.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Owner {
    @Id
    private long id;
    private String name;
    // apartment_id INT
    // resident_id INT
}
