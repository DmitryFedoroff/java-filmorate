package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.util.BaseEntityUtils;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final Map<Long, Film> films = new HashMap<>();

    @GetMapping
    public Collection<Film> findAll() {
        log.debug("Запрос на получение всех фильмов.");
        return films.values();
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        log.info("Попытка создания нового фильма: {}", film);
        film.setId(BaseEntityUtils.getNextId(films));
        films.put(film.getId(), film);
        log.info("Фильм успешно создан: {}", film);
        return film;
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film newFilm) {
        log.info("Попытка обновления данных фильма: {}", newFilm);

        if (!films.containsKey(newFilm.getId())) {
            log.error("Ошибка: Фильм с ID {} не найден.", newFilm.getId());
            throw new NotFoundException("Фильм с указанным ID не найден.");
        }

        films.put(newFilm.getId(), newFilm);
        log.info("Фильм успешно обновлен: {}", newFilm);
        return newFilm;
    }
}
