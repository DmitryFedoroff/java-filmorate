# java-filmorate
Template repository for Filmorate project.

<details>
<summary><b>Схема базы данных проекта Filmorate:</b></summary>

![Схема базы данных проекта Filmorate в формате png](/src/main/docs/intermediate-sql-task.png)

![Оригинал схемы базы данных проекта Filmorate на dbdiagram.io](https://dbdiagram.io/d/intermediate-sql-month-task-66f0d439a0828f8aa6b029fc)


1. **`USERS`** хранит данные пользователей, включая эл. почту, логин, имя и дату рождения.

2. **`FILMS`** хранит данные о фильмах, включая название, описание, дату выхода и длительность. **`FILMS`** связана с **`MPA_CATEGORIES`**, ответственной за возрастные рейтинги фильмов (G, PG, PG-13, R и NC-17). Так как фильмы могут принадлежать к нескольким жанрам одновременно, промежуточная **`FILM_GENRES`** связывает фильмы с жанрами из **`GENRES`**.

3. **`FRIENDS`** хранит связи «дружба» между двумя пользователями. Предусматривает два статуса: «неподтверждённая», когда один пользователь отправил запрос на добавление другого пользователя в друзья, и «подтверждённая», когда второй пользователь согласился на это добавление.

4. **`LIKES` и `RATINGS`** нужны для отслеживания взаимодействий пользователей с фильмами. **`LIKES`** фиксирует лайки, а **`RATINGS`** хранит выставленные оценки.

</details>

<details>
<summary><b>Примеры SQL-запросов к базе данных</b></summary>

#### **1. Получение списка всех пользователей**

```sql
SELECT user_id, email, login, name, birthday FROM users;
```

#### **2. Добавление нового пользователя**

```sql
INSERT INTO users (email, login, name, birthday)
VALUES (?, ?, ?, ?);
```

#### **3. Обновление информации о пользователе**

```sql
UPDATE users
SET name = ?, email = ?
WHERE user_id = ?;
```

#### **4. Получение всех фильмов с их жанрами и рейтингом MPA**

```sql
SELECT
    f.film_id,
    f.name,
    f.description,
    f.release_date,
    f.duration,
    m.name AS mpa_rating,
    GROUP_CONCAT(g.name) AS genres
FROM films f
JOIN mpa_categories m ON f.category_mpa_id = m.category_mpa_id
JOIN film_genres fg ON f.film_id = fg.film_id
JOIN genres g ON fg.genre_id = g.genre_id
GROUP BY
    f.film_id,
    f.name,
    f.description,
    f.release_date,
    f.duration,
    m.name;
```

#### **5. Добавление нового фильма**

```sql
INSERT INTO films (name, description, release_date, duration, category_mpa_id)
VALUES (?, ?, ?, ?, ?);
```

#### **6. Добавление лайка пользователю для фильма**

```sql
ALTER TABLE likes
ADD CONSTRAINT unique_like UNIQUE (user_id, film_id);
```

```sql
INSERT INTO likes (user_id, film_id)
VALUES (?, ?);
```

#### **7. Удаление лайка пользователя для фильма**

```sql
DELETE FROM likes
WHERE user_id = ? AND film_id = ?;
```

#### **8. Получение списка друзей пользователя**

```sql
SELECT
    u.user_id,
    u.name,
    f.status AS friendship_status
FROM users u
JOIN friends f ON u.user_id = f.friend_id
WHERE f.user_id = ?;
```

#### **9. Получение топ-N популярных фильмов (по количеству лайков)**

```sql
SELECT
    f.film_id,
    f.name,
    COUNT(l.user_id) AS likes_count
FROM films f
LEFT JOIN likes l ON f.film_id = l.film_id
GROUP BY
    f.film_id,
    f.name
ORDER BY likes_count DESC
LIMIT ?;
```

#### **10. Добавление нового жанра**

```sql
INSERT INTO genres (name)
VALUES (?);
```

</details>
