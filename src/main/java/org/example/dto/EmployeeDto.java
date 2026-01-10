package org.example.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@AllArgsConstructor
@Getter
@Setter
@ToString
@NoArgsConstructor
/*
тип модел, който използва ентити моделите и комбинира свойствата им гъвкаво, без да ги променя. Последното е важно, защото те са част от persistence layer и са свързани с таблиците. Трябва да работя с DTO модели - data transfer objects. искам да бъда на абстрактното ниво, на което е сървисът. направя този employee DTO модел, който няма да бъде ентити и свързано директно с таблица, а ще бъде само за да изпълнявам заявките като резултат. DAO layer-a вече трябва да работи с dto обектите. DTO трябва да има конструктор, който да позволява да бъдат записани свойствата и да бъде направена връзката между ентити модела и DTO модела. DTO моделът е за да получавам резултати от заявки, за да ъпдейтвам свойства и да създавам нови записи в таблици, когато имплементирам DAO layer.
 */
public class EmployeeDto {
    private long id;
    @NotBlank(message = "Employee name is required")
    @Size(min = 2, max = 30)
    private String name;
    @Min(value = 0, message = "Age must be >= 0")
    private int age;
    @Positive
    @NotNull
    private Long company_id;
}
