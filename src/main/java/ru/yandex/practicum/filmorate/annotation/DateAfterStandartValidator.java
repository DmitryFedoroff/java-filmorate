package ru.yandex.practicum.filmorate.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

public class DateAfterStandartValidator implements ConstraintValidator<DateAfterStandart, LocalDate> {

    private LocalDate standartDate;

    @Override
    public void initialize(DateAfterStandart constraintAnnotation) {
        this.standartDate = LocalDate.parse(constraintAnnotation.standartDate());
    }

    @Override
    public boolean isValid(LocalDate localDate, ConstraintValidatorContext context) {
        if (localDate == null) {
            return true;
        }
        return !localDate.isBefore(standartDate);
    }
}
