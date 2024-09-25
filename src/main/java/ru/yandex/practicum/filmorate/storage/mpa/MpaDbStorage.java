package ru.yandex.practicum.filmorate.storage.mpa;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.MpaCategory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class MpaDbStorage implements MpaStorage {

    private final JdbcTemplate jdbcTemplate;

    public MpaDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<MpaCategory> findAll() {
        String sql = "SELECT * FROM MPA_CATEGORIES ORDER BY category_mpa_id";
        return jdbcTemplate.query(sql, this::mapRowToMpa);
    }

    @Override
    public Optional<MpaCategory> findById(Integer id) {
        String sql = "SELECT * FROM MPA_CATEGORIES WHERE category_mpa_id = ?";
        List<MpaCategory> mpaCategories = jdbcTemplate.query(sql, this::mapRowToMpa, id);
        if (mpaCategories.isEmpty()) {
            throw new NotFoundException("Категория MPA с ID " + id + " не найдена.");
        }
        return mpaCategories.stream().findFirst();
    }

    private MpaCategory mapRowToMpa(ResultSet rs, int rowNum) throws SQLException {
        MpaCategory mpa = new MpaCategory();
        mpa.setId(rs.getInt("category_mpa_id"));
        mpa.setName(rs.getString("name"));
        return mpa;
    }
}