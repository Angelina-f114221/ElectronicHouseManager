package org.example.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;
// може да се използва само на полета (например в моя случай - име)
@Target({ElementType.FIELD})
// запазва информацията при runtime (при изпълнение)
@Retention(RetentionPolicy.RUNTIME)
// свързва този анотационен клас с другия, който е за валидиране (InvalidCompanyNamesValidator.class)
@Constraint(validatedBy = InvalidCompanyNamesValidator.class)
public @interface InvalidCompanyNames {
    // масив от невалидни имена (test, demo - Company)
    String[] value();
    // съобщение при грешка
    String message() default "Invalid company name";
    // кога се валидира (при създаване, ъпдейт, триене)
    Class<?>[] groups() default {};
    // за допълнителна иннформация към грешката
    Class<? extends Payload>[] payload() default {};
}
