-- Dropping dependent tables if they exist
DROP TABLE IF EXISTS RATINGS;
DROP TABLE IF EXISTS FRIENDS;
DROP TABLE IF EXISTS LIKES;
DROP TABLE IF EXISTS FILM_GENRES;
DROP TABLE IF EXISTS GENRES;
DROP TABLE IF EXISTS FILMS;
DROP TABLE IF EXISTS MPA_CATEGORIES;
DROP TABLE IF EXISTS USERS;

-- Creating the USERS table
CREATE TABLE USERS (
    user_id INTEGER PRIMARY KEY AUTO_INCREMENT,
    email VARCHAR(255) NOT NULL UNIQUE,
    login VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    birthday DATE NOT NULL
);

-- Creating the MPA_CATEGORIES table
CREATE TABLE MPA_CATEGORIES (
    category_mpa_id INTEGER PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL UNIQUE
);

-- Creating the FILMS table
CREATE TABLE FILMS (
    film_id INTEGER PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(200) NOT NULL,
    release_date DATE NOT NULL,
    duration INTEGER NOT NULL,
    category_mpa_id INTEGER NOT NULL,
    FOREIGN KEY (category_mpa_id) REFERENCES MPA_CATEGORIES(category_mpa_id)
);

-- Creating the GENRES table
CREATE TABLE GENRES (
    genre_id INTEGER PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL UNIQUE
);

-- Creating the FILM_GENRES table for many-to-many relationship between films and genres
CREATE TABLE FILM_GENRES (
    film_id INTEGER,
    genre_id INTEGER,
    PRIMARY KEY (film_id, genre_id),
    FOREIGN KEY (film_id) REFERENCES FILMS(film_id) ON DELETE CASCADE,
    FOREIGN KEY (genre_id) REFERENCES GENRES(genre_id) ON DELETE CASCADE
);

-- Creating the LIKES table for tracking which users liked which films
CREATE TABLE LIKES (
    user_id INTEGER,
    film_id INTEGER,
    PRIMARY KEY (user_id, film_id),
    FOREIGN KEY (user_id) REFERENCES USERS(user_id) ON DELETE CASCADE,
    FOREIGN KEY (film_id) REFERENCES FILMS(film_id) ON DELETE CASCADE
);

-- Creating the FRIENDS table for tracking friendships between users
CREATE TABLE FRIENDS (
    user_id INTEGER,
    friend_id INTEGER,
    status VARCHAR(20) NOT NULL,
    PRIMARY KEY (user_id, friend_id),
    FOREIGN KEY (user_id) REFERENCES USERS(user_id) ON DELETE CASCADE,
    FOREIGN KEY (friend_id) REFERENCES USERS(user_id) ON DELETE CASCADE
);

-- Creating the RATINGS table for tracking user ratings for films
CREATE TABLE RATINGS (
    user_id INTEGER,
    film_id INTEGER,
    rating INTEGER NOT NULL,
    PRIMARY KEY (user_id, film_id),
    FOREIGN KEY (user_id) REFERENCES USERS(user_id) ON DELETE CASCADE,
    FOREIGN KEY (film_id) REFERENCES FILMS(film_id) ON DELETE CASCADE
);
