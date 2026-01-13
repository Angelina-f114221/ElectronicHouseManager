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
    // стратегия, според която id автоматично започва от стойност 1 и се инкрементира с 1 всеки път
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // не може да е null
    private long id;
}