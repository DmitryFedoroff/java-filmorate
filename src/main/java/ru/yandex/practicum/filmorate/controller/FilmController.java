package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exception.ErrorResponse;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;

@RestController
@RequestMapping("/films")
@Slf4j
@RequiredArgsConstructor
public class FilmController {

    private final FilmService filmService;

    @GetMapping
    public List<Film> findAll() {
        log.debug("Запрос на получение всех фильмов.");
        return filmService.findAll();
    }

    @GetMapping("/{id}")
    public Film findById(@PathVariable Long id) {
        log.debug("Запрос на получение фильма с ID: {}", id);
        return filmService.findById(id);
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody Film film) {
        try {
            log.debug("Попытка создания нового фильма: {}", film);
            Film createdFilm = filmService.create(film);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdFilm);
        } catch (ValidationException e) {
            log.error("Ошибка валидации при создании фильма: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(e.getMessage()));
        } catch (NotFoundException e) {
            log.error("Ошибка поиска при создании фильма: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(e.getMessage()));
        }
    }

    @PutMapping
    public ResponseEntity<?> update(@Valid @RequestBody Film film) {
        try {
            log.debug("Попытка обновления фильма: {}", film);
            Film updatedFilm = filmService.update(film);
            return ResponseEntity.ok(updatedFilm);
        } catch (ValidationException e) {
            log.error("Ошибка валидации при обновлении фильма: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(e.getMessage()));
        } catch (NotFoundException e) {
            log.error("Ошибка поиска при обновлении фильма: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(e.getMessage()));
        }
    }

    @PutMapping("/{id}/like/{userId}")
    public ResponseEntity<?> addLike(@PathVariable Long id, @PathVariable Long userId) {
        try {
            log.debug("Добавление лайка от пользователя с ID {} фильму с ID {}", userId, id);
            Film updatedFilm = filmService.addLike(id, userId);
            return ResponseEntity.ok(updatedFilm);
        } catch (NotFoundException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(e.getMessage()));
        }
    }

    @DeleteMapping("/{id}/like/{userId}")
    public ResponseEntity<?> removeLike(@PathVariable Long id, @PathVariable Long userId) {
        try {
            log.debug("Удаление лайка от пользователя с ID {} у фильма с ID {}", userId, id);
            Film updatedFilm = filmService.removeLike(id, userId);
            return ResponseEntity.ok(updatedFilm);
        } catch (NotFoundException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(e.getMessage()));
        }
    }

    @GetMapping("/popular")
    public List<Film> getPopularFilms(@RequestParam(defaultValue = "10") int count) {
        log.debug("Запрос на получение топ {} популярных фильмов.", count);
        return filmService.getPopularFilms(count);
    }
}