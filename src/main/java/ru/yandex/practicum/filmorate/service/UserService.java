package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class UserService {

    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User addFriend(Long userId, Long friendId) {
        User user = getUserById(userId);
        User friend = getUserById(friendId);

        user.getFriends().add(friendId);
        friend.getFriends().add(userId);

        userStorage.update(user);
        userStorage.update(friend);

        return user;
    }

    public User removeFriend(Long userId, Long friendId) {
        User user = getUserById(userId);
        User friend = getUserById(friendId);

        user.getFriends().remove(friendId);
        friend.getFriends().remove(userId);

        userStorage.update(user);
        userStorage.update(friend);

        return user;
    }

    public List<User> getFriends(Long userId) {
        User user = getUserById(userId);
        Set<Long> friendIds = user.getFriends();
        List<User> friends = new ArrayList<>();

        for (Long id : friendIds) {
            friends.add(getUserById(id));
        }

        return friends;
    }

    public List<User> getCommonFriends(Long userId, Long otherId) {
        User user = getUserById(userId);
        User otherUser = getUserById(otherId);

        Set<Long> commonFriendIds = user.getFriends();
        commonFriendIds.retainAll(otherUser.getFriends());

        List<User> commonFriends = new ArrayList<>();
        for (Long id : commonFriendIds) {
            commonFriends.add(getUserById(id));
        }

        return commonFriends;
    }

    public List<User> findAll() {
        return new ArrayList<>(userStorage.findAll());
    }

    public User findById(Long id) {
        return getUserById(id);
    }

    public User create(User user) {
        return userStorage.create(user);
    }

    public User update(User user) {
        return userStorage.update(user);
    }

    private User getUserById(Long userId) {
        return userStorage.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с ID " + userId + " не найден."));
    }
}
