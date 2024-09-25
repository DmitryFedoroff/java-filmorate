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
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exception.ErrorResponse;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<User> getUsers() {
        log.debug("Запрос на получение всех пользователей.");
        return userService.findAll();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        log.debug("Запрос на получение пользователя с ID: {}", id);
        return userService.findById(id);
    }

    @PostMapping
    public ResponseEntity<User> create(@Valid @RequestBody User user) {
        log.debug("Попытка создания нового пользователя: {}", user);
        User createdUser = userService.create(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        log.debug("Попытка обновления пользователя: {}", user);
        return userService.update(user);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public ResponseEntity<?> addFriend(@PathVariable Long id, @PathVariable Long friendId) {
        try {
            log.debug("Отправка запроса на дружбу от пользователя с ID {} пользователю с ID {}", id, friendId);
            User updatedUser = userService.addFriend(id, friendId);
            return ResponseEntity.ok(updatedUser);
        } catch (NotFoundException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(e.getMessage()));
        }
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public ResponseEntity<?> removeFriend(@PathVariable Long id, @PathVariable Long friendId) {
        try {
            log.debug("Удаление пользователя с ID {} из друзей пользователя с ID {}", friendId, id);
            User updatedUser = userService.removeFriend(id, friendId);
            return ResponseEntity.ok(updatedUser);
        } catch (NotFoundException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(e.getMessage()));
        }
    }

    @GetMapping("/{id}/friends")
    public List<User> getFriends(@PathVariable Long id) {
        log.debug("Запрос на получение списка друзей пользователя с ID: {}", id);
        return userService.getFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable Long id, @PathVariable Long otherId) {
        log.debug("Запрос на получение общих друзей между пользователями с ID: {} и ID: {}", id, otherId);
        return userService.getCommonFriends(id, otherId);
    }
}