package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserValidationTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @Test
    void shouldFailWhenEmailIsInvalid() {
        User user = new User();
        user.setEmail("invalidEmail");
        user.setLogin("validLogin");
        user.setName("Valid Name");
        user.setBirthday(LocalDate.of(2000, 1, 1));

        assertFalse(validator.validate(user).isEmpty(), "Ожидалась ошибка: некорректный email");
    }

    @Test
    void shouldFailWhenEmailHasInvalidCharacters() {
        User user = new User();
        user.setEmail("invalid@em@ail.com");
        user.setLogin("validLogin");
        user.setName("Valid Name");
        user.setBirthday(LocalDate.of(2000, 1, 1));

        assertFalse(validator.validate(user).isEmpty(), "Ожидалась ошибка: email с некорректными символами");
    }

    @Test
    void shouldPassWhenEmailIsValid() {
        User user = new User();
        user.setEmail("valid.email@example.com");
        user.setLogin("validLogin");
        user.setName("Valid Name");
        user.setBirthday(LocalDate.of(2000, 1, 1));

        assertTrue(validator.validate(user).isEmpty(), "Ожидалась успешная валидация при корректном email");
    }

    @Test
    void shouldFailWhenLoginContainsSpaces() {
        User user = new User();
        user.setEmail("valid.email@example.com");
        user.setLogin("invalid login");
        user.setName("Valid Name");
        user.setBirthday(LocalDate.of(2000, 1, 1));

        assertFalse(validator.validate(user).isEmpty(), "Ожидалась ошибка: логин содержит пробелы");
    }

    @Test
    void shouldPassWhenNameIsEmpty() {
        User user = new User();
        user.setEmail("valid.email@example.com");
        user.setLogin("validLogin");
        user.setName("");
        user.setBirthday(LocalDate.of(2000, 1, 1));

        assertTrue(validator.validate(user).isEmpty(), "Ожидалась успешная валидация при пустом имени");
    }

    @Test
    void shouldFailWhenBirthdayIsInTheFuture() {
        User user = new User();
        user.setEmail("valid.email@example.com");
        user.setLogin("validLogin");
        user.setName("Valid Name");
        user.setBirthday(LocalDate.now().plusDays(1));

        assertFalse(validator.validate(user).isEmpty(), "Ожидалась ошибка: дата рождения в будущем");
    }

    @Test
    void shouldPassWhenAllFieldsAreValid() {
        User user = new User();
        user.setEmail("valid.email@example.com");
        user.setLogin("validLogin");
        user.setName("Valid Name");
        user.setBirthday(LocalDate.of(2000, 1, 1));

        assertTrue(validator.validate(user).isEmpty(), "Ожидалась успешная валидация при корректных данных");
    }

    @Test
    void shouldFailWhenEmailIsEmpty() {
        User user = new User();
        user.setEmail("");
        user.setLogin("validLogin");
        user.setName("Valid Name");
        user.setBirthday(LocalDate.of(2000, 1, 1));

        assertFalse(validator.validate(user).isEmpty(), "Ожидалась ошибка: электронная почта пустая");
    }

    @Test
    void shouldFailWhenLoginIsEmpty() {
        User user = new User();
        user.setEmail("valid.email@example.com");
        user.setLogin("");
        user.setName("Valid Name");
        user.setBirthday(LocalDate.of(2000, 1, 1));

        assertFalse(validator.validate(user).isEmpty(), "Ожидалась ошибка: логин пустой");
    }
}
