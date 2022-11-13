MERGE INTO MPA KEY(ID)
    VALUES (1, 'G'),
    (2, 'PG'),
    (3, 'PG-13'),
    (4, 'R'),
    (5, 'NC-17');

MERGE INTO genres KEY(ID)
    VALUES (1, 'Комедия'),
    (2, 'Драма'),
    (3, 'Мультфильм'),
    (4, 'Триллер'),
    (5, 'Документальный'),
    (6, 'Боевик');

MERGE INTO friendship_statuses KEY(ID)
    VALUES (1, 'waiting'),
           (2, 'agree');

/*DELETE FROM friends;
DELETE FROM likes;
DELETE FROM users;
DELETE FROM film_genres;
DELETE FROM films;

ALTER TABLE users ALTER COLUMN id RESTART WITH 1;
ALTER TABLE films ALTER COLUMN id RESTART WITH 1;*/