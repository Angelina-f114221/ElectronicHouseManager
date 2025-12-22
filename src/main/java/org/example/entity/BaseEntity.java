package org.example.entity;

import jakarta.persistence.*;

/* абстракция, която служи за стратегия за генериране; този клас не е entity, защото няма да има таблица за него. другите класове няма да имат описани в тях първични ключове, защото ще екстендват този клас с неговите анотации.*/
@MappedSuperclass
public class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
}
