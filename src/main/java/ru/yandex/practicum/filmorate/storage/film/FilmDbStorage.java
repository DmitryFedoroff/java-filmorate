package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MpaCategory;
import ru.yandex.practicum.filmorate.storage.AbstractDbStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public class FilmDbStorage extends AbstractDbStorage<Film> implements FilmStorage {

    @Autowired
    public FilmDbStorage(org.springframework.jdbc.core.JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    public Film create(Film film) {
        String sql = "INSERT INTO FILMS (name, description, release_date, duration, category_mpa_id) " +
                "VALUES (:name, :description, :releaseDate, :duration, :categoryMpaId)";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", film.getName())
                .addValue("description", film.getDescription())
                .addValue("releaseDate", film.getReleaseDate())
                .addValue("duration", film.getDuration())
                .addValue("categoryMpaId", film.getMpa().getId());

        film.setId(insertAndReturnId(sql, params, "film_id"));
        saveFilmGenres(film);
        return findById(film.getId()).orElseThrow(() -> new NotFoundException("Фильм не найден после создания"));
    }

    @Override
    public Film update(Film film) {
        String sql = "UPDATE FILMS SET name = :name, description = :description, release_date = :releaseDate, " +
                "duration = :duration, category_mpa_id = :categoryMpaId WHERE film_id = :filmId";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", film.getName())
                .addValue("description", film.getDescription())
                .addValue("releaseDate", film.getReleaseDate())
                .addValue("duration", film.getDuration())
                .addValue("categoryMpaId", film.getMpa().getId())
                .addValue("filmId", film.getId());

        update(sql, params);
        deleteFilmGenres(film.getId());
        saveFilmGenres(film);
        return findById(film.getId()).orElseThrow(() -> new NotFoundException("Фильм не найден после обновления"));
    }

    @Override
    public List<Film> findAll() {
        String sql = "SELECT f.*, m.name as mpa_name FROM FILMS f JOIN MPA_CATEGORIES m ON f.category_mpa_id = m.category_mpa_id";
        return query(sql, new MapSqlParameterSource());
    }

    @Override
    public Optional<Film> findById(Long id) {
        String sql = "SELECT f.*, m.name as mpa_name FROM FILMS f JOIN MPA_CATEGORIES m ON f.category_mpa_id = m.category_mpa_id WHERE f.film_id = :filmId";
        MapSqlParameterSource params = new MapSqlParameterSource().addValue("filmId", id);
        return queryForObject(sql, params);
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM FILMS WHERE film_id = :filmId";
        MapSqlParameterSource params = new MapSqlParameterSource().addValue("filmId", id);
        update(sql, params);
    }

    @Override
    public Film addLike(Long filmId, Long userId) {
        String sql = "INSERT INTO LIKES (film_id, user_id) VALUES (:filmId, :userId)";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("filmId", filmId)
                .addValue("userId", userId);
        update(sql, params);
        return findById(filmId).orElseThrow(() -> new NotFoundException("Фильм не найден после добавления лайка"));
    }

    @Override
    public Film removeLike(Long filmId, Long userId) {
        String sql = "DELETE FROM LIKES WHERE film_id = :filmId AND user_id = :userId";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("filmId", filmId)
                .addValue("userId", userId);
        update(sql, params);
        return findById(filmId).orElseThrow(() -> new NotFoundException("Фильм не найден после удаления лайка"));
    }

    @Override
    public void deleteFilmGenres(Long filmId) {
        String sql = "DELETE FROM FILM_GENRES WHERE film_id = :filmId";
        MapSqlParameterSource params = new MapSqlParameterSource().addValue("filmId", filmId);
        update(sql, params);
    }

    @Override
    public void saveFilmGenres(Film film) {
        if (film.getGenres() != null && !film.getGenres().isEmpty()) {
            String sql = "INSERT INTO FILM_GENRES (film_id, genre_id) VALUES (:filmId, :genreId)";
            namedParameterJdbcTemplate.batchUpdate(sql, film.getGenres().stream()
                    .distinct()
                    .map(genre -> new MapSqlParameterSource()
                            .addValue("filmId", film.getId())
                            .addValue("genreId", genre.getId())).toArray(MapSqlParameterSource[]::new));
        }
    }

    @Override
    protected Film mapRowToEntity(ResultSet rs, int rowNum) throws SQLException {
        Film film = new Film();
        film.setId(rs.getLong("film_id"));
        film.setName(rs.getString("name"));
        film.setDescription(rs.getString("description"));
        film.setReleaseDate(rs.getDate("release_date").toLocalDate());
        film.setDuration(rs.getLong("duration"));

        MpaCategory mpa = new MpaCategory();
        mpa.setId(rs.getInt("category_mpa_id"));
        mpa.setName(rs.getString("mpa_name"));
        film.setMpa(mpa);

        film.setGenres(getFilmGenres(film.getId()));
        film.setLikes(getFilmLikes(film.getId()));
        return film;
    }

    private List<Genre> getFilmGenres(Long filmId) {
        String sql = "SELECT g.genre_id, g.name FROM FILM_GENRES fg " +
                "JOIN GENRES g ON fg.genre_id = g.genre_id " +
                "WHERE fg.film_id = :filmId " +
                "ORDER BY g.genre_id";
        MapSqlParameterSource params = new MapSqlParameterSource().addValue("filmId", filmId);
        return namedParameterJdbcTemplate.query(sql, params, (rs, rowNum) -> {
            Genre genre = new Genre();
            genre.setId(rs.getInt("genre_id"));
            genre.setName(rs.getString("name"));
            return genre;
        });
    }

    private Set<Long> getFilmLikes(Long filmId) {
        String sql = "SELECT user_id FROM LIKES WHERE film_id = :filmId";
        MapSqlParameterSource params = new MapSqlParameterSource().addValue("filmId", filmId);
        return new HashSet<>(namedParameterJdbcTemplate.query(sql, params, (rs, rowNum) -> rs.getLong("user_id")));
    }
}