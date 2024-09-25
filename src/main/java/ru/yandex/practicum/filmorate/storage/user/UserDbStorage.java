package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.AbstractDbStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.HashSet;

@Repository
public class UserDbStorage extends AbstractDbStorage<User> implements UserStorage {

    @Autowired
    public UserDbStorage(org.springframework.jdbc.core.JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    public User create(User user) {
        String sql = "INSERT INTO USERS (email, login, name, birthday) VALUES (:email, :login, :name, :birthday)";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("email", user.getEmail())
                .addValue("login", user.getLogin())
                .addValue("name", user.getName())
                .addValue("birthday", user.getBirthday());

        user.setId(insertAndReturnId(sql, params, "user_id"));
        return user;
    }

    @Override
    public User update(User user) {
        String sql = "UPDATE USERS SET email = :email, login = :login, name = :name, birthday = :birthday WHERE user_id = :userId";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("email", user.getEmail())
                .addValue("login", user.getLogin())
                .addValue("name", user.getName())
                .addValue("birthday", user.getBirthday())
                .addValue("userId", user.getId());

        update(sql, params);
        return findById(user.getId()).orElseThrow(() -> new NotFoundException("Пользователь не найден после обновления"));
    }

    @Override
    public List<User> findAll() {
        String sql = "SELECT * FROM USERS";
        return query(sql, new MapSqlParameterSource());
    }

    @Override
    public Optional<User> findById(Long id) {
        String sql = "SELECT * FROM USERS WHERE user_id = :userId";
        MapSqlParameterSource params = new MapSqlParameterSource().addValue("userId", id);
        return queryForObject(sql, params);
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM USERS WHERE user_id = :userId";
        MapSqlParameterSource params = new MapSqlParameterSource().addValue("userId", id);
        update(sql, params);
    }

    @Override
    public User addFriend(Long userId, Long friendId) {
        String sql = "INSERT INTO FRIENDS (user_id, friend_id, status) VALUES (:userId, :friendId, 'pending')";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("userId", userId)
                .addValue("friendId", friendId);
        update(sql, params);
        return findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден после добавления друга"));
    }

    @Override
    public User confirmFriendship(Long userId, Long friendId) {
        String sql = "UPDATE FRIENDS SET status = 'confirmed' WHERE user_id = :userId AND friend_id = :friendId";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("userId", userId)
                .addValue("friendId", friendId);
        update(sql, params);
        return findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден после подтверждения дружбы"));
    }

    @Override
    public User removeFriend(Long userId, Long friendId) {
        String sql = "DELETE FROM FRIENDS WHERE user_id = :userId AND friend_id = :friendId";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("userId", userId)
                .addValue("friendId", friendId);
        update(sql, params);
        return findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден после удаления друга"));
    }

    @Override
    protected User mapRowToEntity(ResultSet rs, int rowNum) throws SQLException {
        User user = new User();
        user.setId(rs.getLong("user_id"));
        user.setEmail(rs.getString("email"));
        user.setLogin(rs.getString("login"));
        user.setName(rs.getString("name"));
        user.setBirthday(rs.getDate("birthday").toLocalDate());
        user.setFriends(getFriendIds(user.getId()));
        return user;
    }

    private Set<Long> getFriendIds(Long userId) {
        String sql = "SELECT friend_id FROM FRIENDS WHERE user_id = :userId";
        MapSqlParameterSource params = new MapSqlParameterSource().addValue("userId", userId);
        return new HashSet<>(namedParameterJdbcTemplate.query(sql, params, (rs, rowNum) -> rs.getLong("friend_id")));
    }
}