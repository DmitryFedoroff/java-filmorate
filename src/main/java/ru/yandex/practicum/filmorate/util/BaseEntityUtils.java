package ru.yandex.practicum.filmorate.util;

import lombok.experimental.UtilityClass;

import java.util.Map;

@UtilityClass
public class BaseEntityUtils {

    public static <T> long getNextId(Map<Long, T> entities) {
        return entities.keySet().stream().mapToLong(id -> id).max().orElse(0) + 1;
    }
}
