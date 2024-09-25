package ru.yandex.practicum.filmorate.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.util.List;
import java.util.Optional;

public abstract class AbstractDbStorage<T> {
    protected final JdbcTemplate jdbcTemplate;
    protected final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public AbstractDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
    }

    protected Long insertAndReturnId(String sql, MapSqlParameterSource params, String idColumnName) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(sql, params, keyHolder, new String[]{idColumnName});
        Number key = keyHolder.getKey();
        if (key != null) {
            return key.longValue();
        } else {
            if (keyHolder.getKeys() != null && keyHolder.getKeys().containsKey(idColumnName)) {
                return ((Number) keyHolder.getKeys().get(idColumnName)).longValue();
            } else {
                throw new IllegalStateException("Не удалось получить сгенерированный ключ");
            }
        }
    }

    protected void update(String sql, MapSqlParameterSource params) {
        namedParameterJdbcTemplate.update(sql, params);
    }

    protected Optional<T> queryForObject(String sql, MapSqlParameterSource params) {
        List<T> results = namedParameterJdbcTemplate.query(sql, params, this::mapRowToEntity);
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    protected List<T> query(String sql, MapSqlParameterSource params) {
        return namedParameterJdbcTemplate.query(sql, params, this::mapRowToEntity);
    }

    protected abstract T mapRowToEntity(java.sql.ResultSet rs, int rowNum) throws java.sql.SQLException;
}
