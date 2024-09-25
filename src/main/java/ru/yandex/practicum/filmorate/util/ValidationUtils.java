package ru.yandex.practicum.filmorate.util;

import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MpaCategory;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public class ValidationUtils {

    public static User validateUserExists(Optional<User> userOptional, Long userId) {
        return userOptional.orElseThrow(() -> new NotFoundException("Пользователь с ID " + userId + " не найден."));
    }

    public static Film validateFilmExists(Optional<Film> filmOptional, Long filmId) {
        return filmOptional.orElseThrow(() -> new NotFoundException("Фильм с ID " + filmId + " не найден."));
    }

    public static void validateUserNotNull(User user) {
        if (user == null) {
            throw new IllegalArgumentException("Пользователь не может быть null");
        }
    }

    public static void validateFilmNotNull(Film film) {
        if (film == null) {
            throw new IllegalArgumentException("Фильм не может быть null");
        }
    }

    public static void validateMpa(MpaCategory mpa) throws ValidationException {
        if (mpa == null || mpa.getId() == null || mpa.getId() < 1 || mpa.getId() > 5) {
            throw new ValidationException("Недопустимый рейтинг MPA");
        }
    }

    public static void validateGenres(List<Genre> genres) throws ValidationException {
        if (genres != null) {
            for (Genre genre : genres) {
                if (genre.getId() == null || genre.getId() < 1 || genre.getId() > 6) {
                    throw new ValidationException("Недопустимый ID жанра: " + genre.getId());
                }
            }
        }
    }
}