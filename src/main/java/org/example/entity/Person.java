package org.example.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "persons")
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
@ToString(callSuper=true)
public abstract class Person extends BaseEntity {
    private String name;
    private int age;
}
