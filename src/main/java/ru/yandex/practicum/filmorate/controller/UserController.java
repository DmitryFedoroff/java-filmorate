package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.DuplicatedDataException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.util.BaseEntityUtils;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final Map<Long, User> users = new HashMap<>();

    private boolean isUserWithEmailExist(String eMail) {
        return users.values().stream().anyMatch(userFromMemory -> userFromMemory.getEmail().equals(eMail));
    }

    @GetMapping
    public Collection<User> getUsers() {
        log.debug("Запрос на получение всех пользователей.");
        return users.values();
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        log.debug("Попытка создания нового пользователя: {}", user);

        if (isUserWithEmailExist(user.getEmail())) {
            log.error("Ошибка: Электронная почта уже используется.");
            throw new DuplicatedDataException("Эта электронная почта уже используется.");
        }

        if (user.getName() == null || user.getName().isBlank()) {
            log.debug("Имя пользователя не указано, используем логин: {}", user.getLogin());
            user.setName(user.getLogin());
        }

        user.setId(BaseEntityUtils.getNextId(users));
        users.put(user.getId(), user);

        log.info("Пользователь успешно создан: {}", user);
        return user;
    }

    @PutMapping
    public User update(@Valid @RequestBody User newUser) {
        log.debug("Попытка обновления данных пользователя: {}", newUser);

        if (newUser.getId() == null) {
            log.error("Ошибка: ID пользователя не указан.");
            throw new ValidationException("ID пользователя должен быть указан.");
        }

        User existingUser = users.get(newUser.getId());
        if (existingUser == null) {
            log.error("Ошибка: Пользователь с ID {} не найден.", newUser.getId());
            throw new NotFoundException("Пользователь с указанным ID не найден.");
        }

        if (isUserWithEmailExist(newUser.getEmail()) && !existingUser.getEmail().equals(newUser.getEmail())) {
            log.error("Ошибка: Электронная почта уже используется другим пользователем.");
            throw new DuplicatedDataException("Эта электронная почта уже используется другим пользователем.");
        }

        existingUser.setEmail(newUser.getEmail());
        existingUser.setLogin(newUser.getLogin());
        if (!newUser.getName().isBlank()) {
            existingUser.setName(newUser.getName());
        }
        existingUser.setBirthday(newUser.getBirthday());

        log.info("Пользователь успешно обновлен: {}", existingUser);
        return existingUser;
    }
}
