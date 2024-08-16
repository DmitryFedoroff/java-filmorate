package ru.yandex.practicum.filmorate.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NoSpacesValidatorImpl.class)
public @interface NoSpacesValidator {
    String message() default "Логин не должен содержать пробелы.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
