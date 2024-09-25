package ru.yandex.practicum.filmorate.storage.mpa;

import ru.yandex.practicum.filmorate.model.MpaCategory;

import java.util.List;
import java.util.Optional;

public interface MpaStorage {
    List<MpaCategory> findAll();

    Optional<MpaCategory> findById(Integer id);
}