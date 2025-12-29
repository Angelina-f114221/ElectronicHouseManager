package org.example.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
@ToString(callSuper=true)
public abstract class Person extends BaseEntity {
    private String name;
    private int age;
}
