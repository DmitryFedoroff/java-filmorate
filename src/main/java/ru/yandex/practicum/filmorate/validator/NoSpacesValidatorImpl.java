package ru.yandex.practicum.filmorate.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NoSpacesValidatorImpl implements ConstraintValidator<NoSpacesValidator, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; // @NotNull будет обрабатывать случаи, когда значение null
        }
        return !value.contains(" ");
    }
}
