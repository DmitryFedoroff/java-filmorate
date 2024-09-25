package ru.yandex.practicum.filmorate.storage.genre;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class GenreDbStorage implements GenreStorage {

    private final JdbcTemplate jdbcTemplate;

    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Genre> findAll() {
        String sql = "SELECT * FROM GENRES ORDER BY genre_id";
        return jdbcTemplate.query(sql, this::mapRowToGenre);
    }

    @Override
    public Optional<Genre> findById(Integer id) {
        String sql = "SELECT * FROM GENRES WHERE genre_id = ?";
        List<Genre> genres = jdbcTemplate.query(sql, this::mapRowToGenre, id);
        if (genres.isEmpty()) {
            throw new NotFoundException("Жанр с ID " + id + " не найден.");
        }
        return genres.stream().findFirst();
    }

    private Genre mapRowToGenre(ResultSet rs, int rowNum) throws SQLException {
        Genre genre = new Genre();
        genre.setId(rs.getInt("genre_id"));
        genre.setName(rs.getString("name"));
        return genre;
    }
}