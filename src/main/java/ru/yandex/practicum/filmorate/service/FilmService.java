package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.util.ValidationUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(@Qualifier("filmDbStorage") FilmStorage filmStorage, @Qualifier("userDbStorage") UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    @Transactional
    public Film addLike(Long filmId, Long userId) {
        Film film = findFilmByIdOrThrow(filmId);
        User user = findUserByIdOrThrow(userId);
        return filmStorage.addLike(film.getId(), user.getId());
    }

    @Transactional
    public Film removeLike(Long filmId, Long userId) {
        Film film = findFilmByIdOrThrow(filmId);
        User user = findUserByIdOrThrow(userId);
        return filmStorage.removeLike(film.getId(), user.getId());
    }

    public List<Film> getPopularFilms(int count) {
        return filmStorage.findAll().stream().sorted((f1, f2) -> Integer.compare(f2.getLikes().size(), f1.getLikes().size())).limit(count).collect(Collectors.toList());
    }

    public List<Film> findAll() {
        return filmStorage.findAll();
    }

    public Film findById(Long id) {
        return findFilmByIdOrThrow(id);
    }

    @Transactional
    public Film create(Film film) throws ValidationException {
        ValidationUtils.validateFilmNotNull(film);
        ValidationUtils.validateMpa(film.getMpa());
        ValidationUtils.validateGenres(film.getGenres());
        Film createdFilm = filmStorage.create(film);
        saveFilmGenres(createdFilm);
        return createdFilm;
    }

    @Transactional
    public Film update(Film film) {
        ValidationUtils.validateFilmNotNull(film);
        findFilmByIdOrThrow(film.getId());
        Film updatedFilm = filmStorage.update(film);
        saveFilmGenres(updatedFilm);
        return updatedFilm;
    }

    private void saveFilmGenres(Film film) {
        filmStorage.deleteFilmGenres(film.getId());
        if (film.getGenres() != null && !film.getGenres().isEmpty()) {
            filmStorage.saveFilmGenres(film);
        }
    }

    private Film findFilmByIdOrThrow(Long id) {
        return filmStorage.findById(id).orElseThrow(() -> new NotFoundException("Фильм с идентификатором " + id + " не найден."));
    }

    private User findUserByIdOrThrow(Long id) {
        return userStorage.findById(id).orElseThrow(() -> new NotFoundException("Пользователь с идентификатором " + id + " не найден."));
    }
}