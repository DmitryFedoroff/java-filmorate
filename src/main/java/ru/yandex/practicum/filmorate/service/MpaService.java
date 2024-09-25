package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.MpaCategory;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;

import java.util.List;

@Service
public class MpaService {

    private final MpaStorage mpaStorage;

    @Autowired
    public MpaService(MpaStorage mpaStorage) {
        this.mpaStorage = mpaStorage;
    }

    public List<MpaCategory> findAll() {
        return mpaStorage.findAll();
    }

    public MpaCategory findById(Integer id) {
        return mpaStorage.findById(id)
                .orElseThrow(() -> new NotFoundException("MPA категория с ID " + id + " не найдена."));
    }
}