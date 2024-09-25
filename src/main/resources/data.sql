-- Deleting all records from dependent tables first
DELETE FROM RATINGS;
DELETE FROM FRIENDS;
DELETE FROM LIKES;
DELETE FROM FILM_GENRES;

-- Deleting records from primary tables
DELETE FROM FILMS;
DELETE FROM GENRES;
DELETE FROM MPA_CATEGORIES;
DELETE FROM USERS;

-- Inserting MPA Categories into the MPA_CATEGORIES table
INSERT INTO MPA_CATEGORIES (name) VALUES
    ('G'),
    ('PG'),
    ('PG-13'),
    ('R'),
    ('NC-17');

-- Inserting Genres into the GENRES table
INSERT INTO GENRES (name) VALUES
    ('Комедия'),
    ('Драма'),
    ('Мультфильм'),
    ('Триллер'),
    ('Документальный'),
    ('Боевик');

-- Inserting Users into the USERS table
INSERT INTO USERS (email, login, name, birthday) VALUES
    ('user1@example.com', 'user1', 'User One', '1990-01-01'),
    ('user2@example.com', 'user2', 'User Two', '1992-02-02');

-- Inserting Films into the FILMS table
INSERT INTO FILMS (name, description, release_date, duration, category_mpa_id) VALUES
    ('Film One', 'Description of Film One', '2020-01-01', 120, 1),
    ('Film Two', 'Description of Film Two', '2021-02-02', 150, 2),
    ('Film Three', 'Description of Film Three', '2022-03-03', 130, 3),
    ('Film Four', 'Description of Film Four', '2023-04-04', 140, 4),
    ('Film Five', 'Description of Film Five', '2024-05-05', 160, 5);

-- Linking Films and Genres in the FILM_GENRES table
INSERT INTO FILM_GENRES (film_id, genre_id) VALUES
    (1, 1),
    (1, 2),
    (2, 3),
    (3, 4),
    (4, 5),
    (5, 6);

-- Adding Likes for Films in the LIKES table
INSERT INTO LIKES (user_id, film_id) VALUES
    (1, 1),
    (2, 1),
    (1, 2),
    (2, 3);

-- Adding Friends in the FRIENDS table (bidirectional friendship)
INSERT INTO FRIENDS (user_id, friend_id, status) VALUES
    (1, 2, 'confirmed'),
    (2, 1, 'confirmed');

-- Adding User Ratings for Films in the RATINGS table
INSERT INTO RATINGS (user_id, film_id, rating) VALUES
    (1, 1, 5),
    (2, 1, 4),
    (1, 2, 3),
    (2, 3, 5);
