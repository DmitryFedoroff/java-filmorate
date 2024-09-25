package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MpaCategory;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({UserDbStorage.class, FilmDbStorage.class})
class FilmorateIntegrationTests {

    @Autowired
    private UserDbStorage userStorage;

    @Autowired
    private FilmDbStorage filmStorage;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    public void setup() {
        jdbcTemplate.execute("DELETE FROM FILM_GENRES");
        jdbcTemplate.execute("DELETE FROM LIKES");
        jdbcTemplate.execute("DELETE FROM FRIENDS");
        jdbcTemplate.execute("DELETE FROM FILMS");
        jdbcTemplate.execute("DELETE FROM USERS");
    }

    @Test
    void testCreateAndFindUserById() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setLogin("testuser");
        user.setName("Test User");
        user.setBirthday(LocalDate.of(1990, 1, 1));

        User savedUser = userStorage.create(user);
        assertThat(savedUser.getId()).isNotNull();

        Optional<User> foundUser = userStorage.findById(savedUser.getId());
        assertThat(foundUser)
                .isPresent()
                .hasValueSatisfying(u -> {
                    assertThat(u.getEmail()).isEqualTo("test@example.com");
                    assertThat(u.getLogin()).isEqualTo("testuser");
                    assertThat(u.getName()).isEqualTo("Test User");
                    assertThat(u.getBirthday()).isEqualTo(LocalDate.of(1990, 1, 1));
                });
    }

    @Test
    void testCreateAndFindFilmById() {
        Film film = new Film();
        film.setName("Test Film");
        film.setDescription("Test Description");
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(120L);
        MpaCategory mpa = new MpaCategory();
        mpa.setId(1);
        film.setMpa(mpa);

        Film savedFilm = filmStorage.create(film);
        assertThat(savedFilm.getId()).isNotNull();

        Optional<Film> foundFilm = filmStorage.findById(savedFilm.getId());
        assertThat(foundFilm)
                .isPresent()
                .hasValueSatisfying(f -> {
                    assertThat(f.getName()).isEqualTo("Test Film");
                    assertThat(f.getDescription()).isEqualTo("Test Description");
                    assertThat(f.getReleaseDate()).isEqualTo(LocalDate.of(2000, 1, 1));
                    assertThat(f.getDuration()).isEqualTo(120L);
                    assertThat(f.getMpa().getId()).isEqualTo(1);
                });
    }

    @Test
    void testFindAllUsers() {
        User user1 = new User();
        user1.setEmail("user1@example.com");
        user1.setLogin("user1");
        user1.setName("User One");
        user1.setBirthday(LocalDate.of(1990, 1, 1));
        userStorage.create(user1);

        User user2 = new User();
        user2.setEmail("user2@example.com");
        user2.setLogin("user2");
        user2.setName("User Two");
        user2.setBirthday(LocalDate.of(1991, 1, 1));
        userStorage.create(user2);

        List<User> users = userStorage.findAll();
        assertThat(users).hasSize(2);
    }

    @Test
    void testFindAllFilms() {
        Film film1 = new Film();
        film1.setName("Film One");
        film1.setDescription("Description One");
        film1.setReleaseDate(LocalDate.of(2000, 1, 1));
        film1.setDuration(120L);
        MpaCategory mpa1 = new MpaCategory();
        mpa1.setId(1);
        film1.setMpa(mpa1);
        filmStorage.create(film1);

        Film film2 = new Film();
        film2.setName("Film Two");
        film2.setDescription("Description Two");
        film2.setReleaseDate(LocalDate.of(2001, 1, 1));
        film2.setDuration(130L);
        MpaCategory mpa2 = new MpaCategory();
        mpa2.setId(2);
        film2.setMpa(mpa2);
        filmStorage.create(film2);

        List<Film> films = filmStorage.findAll();
        assertThat(films).hasSize(2);
    }
}