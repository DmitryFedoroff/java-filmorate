package ru.yandex.practicum.filmorate.util;

import java.util.Map;

public class BaseEntityUtils {

    public static <T> long getNextId(Map<Long, T> entities) {
        return entities.keySet().stream().mapToLong(id -> id).max().orElse(0) + 1;
    }
}
