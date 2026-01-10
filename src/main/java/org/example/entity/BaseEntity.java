package org.example.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/* абстракция, която служи за стратегия за генериране; този клас не е entity и няма да има таблица за него. другите класове няма да имат описани в тях първични ключове, защото ще екстендват този клас с неговите анотации.*/
@MappedSuperclass
@Getter
@Setter
@ToString
public class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
}
