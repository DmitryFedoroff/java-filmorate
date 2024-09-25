package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.util.ValidationUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserStorage userStorage;

    @Autowired
    public UserService(@Qualifier("userDbStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    @Transactional
    public User addFriend(Long userId, Long friendId) {
        User user = findUserByIdOrThrow(userId);
        User friend = findUserByIdOrThrow(friendId);
        return userStorage.addFriend(user.getId(), friend.getId());
    }

    @Transactional
    public User removeFriend(Long userId, Long friendId) {
        User user = findUserByIdOrThrow(userId);
        User friend = findUserByIdOrThrow(friendId);
        return userStorage.removeFriend(user.getId(), friend.getId());
    }

    public List<User> getFriends(Long userId) {
        User user = findUserByIdOrThrow(userId);
        return user.getFriends().stream()
                .map(this::findUserByIdOrThrow)
                .collect(Collectors.toList());
    }

    public List<User> getCommonFriends(Long userId, Long otherId) {
        User user = findUserByIdOrThrow(userId);
        User otherUser = findUserByIdOrThrow(otherId);
        return user.getFriends().stream()
                .filter(otherUser.getFriends()::contains)
                .map(this::findUserByIdOrThrow)
                .collect(Collectors.toList());
    }

    public List<User> findAll() {
        return userStorage.findAll();
    }

    public User findById(Long id) {
        return findUserByIdOrThrow(id);
    }

    @Transactional
    public User create(User user) {
        ValidationUtils.validateUserNotNull(user);
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        return userStorage.create(user);
    }

    @Transactional
    public User update(User user) {
        ValidationUtils.validateUserNotNull(user);
        findUserByIdOrThrow(user.getId());
        return userStorage.update(user);
    }

    private User findUserByIdOrThrow(Long id) {
        return userStorage.findById(id).orElseThrow(() -> new NotFoundException("Пользователь с идентификатором " + id + " не найден."));
    }
}