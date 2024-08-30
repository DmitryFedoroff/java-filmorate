package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Service
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    private final Map<Long, Integer> likesCount = new HashMap<>();
    private final TreeMap<Integer, Film> sortedFilms = new TreeMap<>(Collections.reverseOrder());

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        initLikesCount();
    }

    private void initLikesCount() {
        for (Film film : filmStorage.findAll()) {
            int likes = film.getLikes().size();
            likesCount.put(film.getId(), likes);
            sortedFilms.put(likes, film);
        }
    }

    public Film addLike(Long filmId, Long userId) {
        User user = userStorage.findById(userId);
        if (user == null) {
            throw new NotFoundException("Пользователь с ID " + userId + " не найден.");
        }

        Film film = filmStorage.findById(filmId);
        film.getLikes().add(userId);
        updateFilmLikes(film, 1);
        return filmStorage.update(film);
    }

    public Film removeLike(Long filmId, Long userId) {
        User user = userStorage.findById(userId);
        if (user == null) {
            throw new NotFoundException("Пользователь с ID " + userId + " не найден.");
        }

        Film film = filmStorage.findById(filmId);
        film.getLikes().remove(userId);
        updateFilmLikes(film, -1);
        return filmStorage.update(film);
    }

    private void updateFilmLikes(Film film, int delta) {
        int oldLikes = likesCount.getOrDefault(film.getId(), 0);
        int newLikes = oldLikes + delta;

        likesCount.put(film.getId(), newLikes);

        sortedFilms.remove(oldLikes);
        sortedFilms.put(newLikes, film);
    }

    public List<Film> getPopularFilms(int count) {
        return sortedFilms.values().stream()
                .limit(count)
                .collect(Collectors.toList());
    }
}
