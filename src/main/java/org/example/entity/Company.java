package org.example.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

/*
представлява ентити клас, който ще отговаря за таблица в базата и за моделите на данните ни. За да може класа компания да отговаря за таблица, трябва да бъде означен с entity анотация. ентити моделът служи за определяне на структурата на таблицата и за манипулиране на данните. първичният ключ трябва да се анотира изрично, иначе ще има грешка. тази entity анотация идва от jaкарта и persistance подмодула. това е технологичен компонент, който осигурява да се ползват анотации за връзката между обектния и релационния модел и след това Hibernate работи с тях.
 */
@Entity
public class Company {
    // анотирам ID-то, което също е от Jakarta
    @Id
    private long id;
    private String name;
}
