package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.util.BaseEntityUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Long, Film> films = new HashMap<>();

    @Override
    public Film create(Film film) {
        film.setId(BaseEntityUtils.getNextId(films));
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film update(Film film) {
        if (!films.containsKey(film.getId())) {
            throw new NotFoundException("Фильм с ID " + film.getId() + " не найден.");
        }
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public List<Film> findAll() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Optional<Film> findById(Long id) {
        return Optional.ofNullable(films.get(id));
    }

    @Override
    public void deleteById(Long id) {
        if (!films.containsKey(id)) {
            throw new NotFoundException("Фильм с ID " + id + " не найден.");
        }
        films.remove(id);
    }

    @Override
    public Film addLike(Long filmId, Long userId) {
        Film film = findById(filmId).orElseThrow(() -> new NotFoundException("Фильм с ID " + filmId + " не найден."));
        film.getLikes().add(userId);
        return film;
    }

    @Override
    public Film removeLike(Long filmId, Long userId) {
        Film film = findById(filmId).orElseThrow(() -> new NotFoundException("Фильм с ID " + filmId + " не найден."));
        film.getLikes().remove(userId);
        return film;
    }

    @Override
    public void deleteFilmGenres(Long filmId) {
        Film film = findById(filmId).orElseThrow(() -> new NotFoundException("Фильм с ID " + filmId + " не найден."));
        film.setGenres(new ArrayList<>());
    }

    @Override
    public void saveFilmGenres(Film film) {
        Film existingFilm = findById(film.getId()).orElseThrow(() -> new NotFoundException("Фильм с ID " + film.getId() + " не найден."));
        existingFilm.setGenres(film.getGenres());
    }
}