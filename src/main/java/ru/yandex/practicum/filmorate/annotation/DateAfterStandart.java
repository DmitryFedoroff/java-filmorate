package ru.yandex.practicum.filmorate.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DateAfterStandartValidator.class)
public @interface DateAfterStandart {
    String message() default "Дата должна быть не ранее {standartDate}.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String standartDate() default "1895-12-28";
}
