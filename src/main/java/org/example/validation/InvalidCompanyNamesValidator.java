package org.example.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Arrays;

public class InvalidCompanyNamesValidator implements ConstraintValidator<InvalidCompanyNames, String> {
    private String[] invalid;

    // инициалзира невалидните имена
    @Override
    public void initialize(InvalidCompanyNames constraint) {
        // взема забранените има от анотацията и ги съхранява в променлива масив
        invalid = constraint.value();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        // проверка дали полето е празно
        if (value == null) return true;
        // преобразува масива в List, прави всички невалидни имена с малки букви и проверява дали стойността от полето се съдържа в масива. ако е вярно, то тогава се изписва съобщението. ако пък не, името е валидно.
        return !Arrays.asList(invalid).contains(value.toLowerCase());
    }
}
