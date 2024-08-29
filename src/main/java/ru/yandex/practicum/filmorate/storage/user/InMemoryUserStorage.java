package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.DuplicatedDataException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.util.BaseEntityUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class InMemoryUserStorage implements UserStorage {

    private final Map<Long, User> users = new HashMap<>();
    private final Set<String> emails = new HashSet<>();

    @Override
    public User create(User user) {
        if (emails.contains(user.getEmail())) {
            throw new DuplicatedDataException("Эта электронная почта уже используется.");
        }

        user.setId(BaseEntityUtils.getNextId(users));
        users.put(user.getId(), user);
        emails.add(user.getEmail());

        return user;
    }

    @Override
    public User update(User user) {
        if (user.getId() == null || !users.containsKey(user.getId())) {
            throw new NotFoundException("Пользователь с ID " + user.getId() + " не найден.");
        }

        User existingUser = users.get(user.getId());

        if (!existingUser.getEmail().equals(user.getEmail()) && emails.contains(user.getEmail())) {
            throw new DuplicatedDataException("Эта электронная почта уже используется другим пользователем.");
        }

        emails.remove(existingUser.getEmail());
        existingUser.setEmail(user.getEmail());
        emails.add(user.getEmail());

        existingUser.setLogin(user.getLogin());
        existingUser.setName(user.getName().isBlank() ? existingUser.getLogin() : user.getName());
        existingUser.setBirthday(user.getBirthday());

        return existingUser;
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User findById(Long id) {
        if (!users.containsKey(id)) {
            throw new NotFoundException("Пользователь с ID " + id + " не найден.");
        }
        return users.get(id);
    }

    @Override
    public void deleteById(Long id) {
        if (!users.containsKey(id)) {
            throw new NotFoundException("Пользователь с ID " + id + " не найден.");
        }
        emails.remove(users.get(id).getEmail());
        users.remove(id);
    }
}
