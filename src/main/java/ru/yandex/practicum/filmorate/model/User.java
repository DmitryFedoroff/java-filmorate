package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;

@Data
@EqualsAndHashCode(of = {"id"})
@Slf4j
public class User {
    private Long id;

    @NotBlank(message = "Электронная почта должна быть указана.")
    @Email(message = "Электронная почта должна быть корректной и содержать символ '@'.")
    private String email;

    @NotBlank(message = "Логин должен быть указан.")
    @Pattern(regexp = "\\S+", message = "Логин не должен содержать пробелы.")
    private String login;

    private String name;

    @NotNull(message = "Дата рождения не должна быть null.")
    @PastOrPresent(message = "Дата рождения не может быть в будущем.")
    private LocalDate birthday;
}
