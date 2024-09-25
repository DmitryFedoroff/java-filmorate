package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public interface FilmStorage {
    Film create(Film film);

    Film update(Film film);

    List<Film> findAll();

    Optional<Film> findById(Long id);

    Film addLike(Long filmId, Long userId);

    Film removeLike(Long filmId, Long userId);

    void deleteFilmGenres(Long filmId);

    void saveFilmGenres(Film film);

    void deleteById(Long id);
}
