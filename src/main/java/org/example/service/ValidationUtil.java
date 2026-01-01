package org.example.service;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

import java.util.Set;
import java.util.stream.Collectors;

public final class ValidationUtil {
    private static final Validator VALIDATOR =
            Validation.buildDefaultValidatorFactory().getValidator();

    private ValidationUtil() {}

    public static <T> void validateOrThrow(T dto) {
        Set<ConstraintViolation<T>> violations = VALIDATOR.validate(dto);
        if (!violations.isEmpty()) {
            String msg = violations.stream()
                    .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                    .collect(Collectors.joining("; "));
            throw new IllegalArgumentException(msg);
        }
    }
}
