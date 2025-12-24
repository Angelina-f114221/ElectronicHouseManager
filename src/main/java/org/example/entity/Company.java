package org.example.entity;

import jakarta.persistence.*;
import lombok.ToString;

import java.util.Set;

/*
представлява ентити клас, който ще отговаря за таблица в базата и за моделите на данните ни. За да може класа компания да отговаря за таблица, трябва да бъде означен с entity анотация. ентити моделът служи за определяне на структурата на таблицата и за манипулиране на данните. първичният ключ трябва да се анотира изрично, иначе ще има грешка. тази entity анотация идва от jakarta и persistence подмодула. това е технологичен компонент, който осигурява да се ползват анотации за връзката между обектния и релационния модел и след това Hibernate работи с тях.
 */
@Entity
@ToString(callSuper=true)
public class Company extends BaseEntity {
    // анотирам ID-то, което също е от Jakarta
    // При генериране на записи, id-тата на една таблица са независими от тези на друга. позволява ни да имаме auto increment при code first подхода.
   private String name;

   @OneToMany(mappedBy = "company")
    private Set<Building> buildings;

   @OneToMany(mappedBy = "company")
    private Set<Employee> employees;
}
