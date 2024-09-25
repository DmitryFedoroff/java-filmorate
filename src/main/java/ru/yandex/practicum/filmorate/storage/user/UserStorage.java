package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {
    User create(User user);

    User update(User user);

    List<User> findAll();

    Optional<User> findById(Long id);

    User addFriend(Long userId, Long friendId);

    User confirmFriendship(Long userId, Long friendId);

    User removeFriend(Long userId, Long friendId);

    void deleteById(Long id);
}