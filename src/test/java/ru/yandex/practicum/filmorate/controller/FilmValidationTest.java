package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FilmValidationTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @Test
    void shouldFailWhenNameIsEmpty() {
        Film film = new Film();
        film.setName("");
        film.setDescription("Valid description");
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(120L);
        film.setGenres(List.of("Комедия"));
        film.setMpaRating("G");

        assertFalse(validator.validate(film).isEmpty(), "Ожидалась ошибка: название фильма пустое");
    }

    @Test
    void shouldFailWhenDescriptionIsTooLong() {
        Film film = new Film();
        film.setName("Valid Name");
        film.setDescription("a".repeat(201));
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(120L);
        film.setGenres(List.of("Комедия"));
        film.setMpaRating("G");

        assertFalse(validator.validate(film).isEmpty(), "Ожидалась ошибка: описание фильма слишком длинное");
    }

    @Test
    void shouldFailWhenReleaseDateIsBefore1895() {
        Film film = new Film();
        film.setName("Valid Name");
        film.setDescription("Valid description");
        film.setReleaseDate(LocalDate.of(1895, 12, 27));
        film.setDuration(120L);
        film.setGenres(List.of("Комедия"));
        film.setMpaRating("G");

        assertFalse(validator.validate(film).isEmpty(), "Ожидалась ошибка: дата релиза до 28 декабря 1895 года");
    }

    @Test
    void shouldFailWhenDurationIsZeroOrNegative() {
        Film film = new Film();
        film.setName("Valid Name");
        film.setDescription("Valid description");
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(0L);
        film.setGenres(List.of("Комедия"));
        film.setMpaRating("G");

        assertFalse(validator.validate(film).isEmpty(), "Ожидалась ошибка: продолжительность фильма не может быть нулевой или отрицательной");
    }

    @Test
    void shouldPassWhenAllFieldsAreValid() {
        Film film = new Film();
        film.setName("Valid Name");
        film.setDescription("Valid description");
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(120L);
        film.setGenres(List.of("Комедия"));
        film.setMpaRating("G");

        assertTrue(validator.validate(film).isEmpty(), "Ожидалась успешная валидация при корректных данных");
    }

    @Test
    void shouldPassWhenDescriptionIsExactly200Characters() {
        Film film = new Film();
        film.setName("Valid Name");
        film.setDescription("a".repeat(200));
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(120L);
        film.setGenres(List.of("Комедия"));
        film.setMpaRating("G");

        assertTrue(validator.validate(film).isEmpty(), "Ожидалась успешная валидация при описании длиной ровно 200 символов");
    }
}
