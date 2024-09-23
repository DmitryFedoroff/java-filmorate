package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.annotation.DateAfterStandart;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.List;

@Data
@EqualsAndHashCode(of = {"id"})
@Slf4j
public class Film {
    private Long id;

    @NotBlank(message = "Название фильма не должно быть пустым.")
    private String name;

    @NotNull(message = "Описание фильма не должно быть null.")
    @Size(max = 200, message = "Описание фильма не должно превышать 200 символов.")
    private String description;

    @NotNull(message = "Дата релиза не должна быть null.")
    @DateAfterStandart(message = "Дата релиза фильма должна быть не раньше {standartDate}.")
    private LocalDate releaseDate;

    @NotNull(message = "Продолжительность фильма не должна быть null.")
    @Positive(message = "Продолжительность фильма должна быть положительным числом.")
    private Long duration;

    @JsonIgnore
    private Set<Long> likes = new HashSet<>();

    @NotNull(message = "Жанр фильма должен быть указан.")
    private List<String> genres;

    @NotNull(message = "Возрастной рейтинг фильма должен быть указан.")
    private String mpaRating;
}
