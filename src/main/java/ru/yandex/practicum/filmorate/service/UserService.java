package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User addFriend(Long userId, Long friendId) {
        User user = userStorage.findById(userId);
        User friend = userStorage.findById(friendId);

        user.getFriends().add(friendId);
        friend.getFriends().add(userId);

        userStorage.update(user);
        userStorage.update(friend);

        return user;
    }

    public User removeFriend(Long userId, Long friendId) {
        User user = userStorage.findById(userId);
        User friend = userStorage.findById(friendId);

        user.getFriends().remove(friendId);
        friend.getFriends().remove(userId);

        userStorage.update(user);
        userStorage.update(friend);

        return user;
    }

    public List<User> getFriends(Long userId) {
        User user = userStorage.findById(userId);
        return new ArrayList<>(user.getFriends().stream()
                .map(userStorage::findById)
                .collect(Collectors.toMap(User::getId, Function.identity(), (existing, replacement) -> existing, HashMap::new))
                .values());
    }

    public List<User> getCommonFriends(Long userId, Long otherId) {
        User user = userStorage.findById(userId);
        User otherUser = userStorage.findById(otherId);

        Set<Long> commonFriendIds = new HashSet<>(user.getFriends());
        commonFriendIds.retainAll(otherUser.getFriends());

        return commonFriendIds.stream()
                .map(userStorage::findById)
                .collect(Collectors.toList());
    }
}
