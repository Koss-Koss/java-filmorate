# java-filmorate

**Filmorate** - бэкенд сервиса для работы с фильмами и оценками пользователей.

## Техническое описание

### Модели
* **Film** - фильмы
* **User** - пользователи
* **Genre** - жанры
* **RatingMPA** - рейтинг, присвоенный MPA
* **Likes** - информация о лайках пользователей к фильмам
* **Friends** - информация об отношении дружбы пользователей друг с другом

### Связи между моделями (ER-diagram)
![ER diagram](filmorate_ER_diagram.png)

**Примеры запросов для основных операций**
* Получение списка всех фильмов
```
SELECT *
FROM film;
```

* Получение 10 наиболее популярных фильмов (id и название) по количеству лайков
```
SELECT film_id,
       name
FROM film
ORDER BY rate DESC
LIMIT 10;
```

* Получение пользователя с id=17
```
SELECT *
FROM user
WHERE user_id=17;
```

* Получение списка друзей (id и login) пользователя с id=17
```
SELECT u.user_id,
       u.login
FROM user u
INNER JOIN friends f ON f.user_id = 17 AND u.user_id = f.friend_id;
```

* Получение списка общих друзей (id и login) пользователей с id=3 и id=17
```
SELECT u.user_id,
       u.login
FROM user u
INNER JOIN friends f ON (f.user_id = 3 AND u.user_id = f.friend_id) AND
                        (f.user_id = 17 AND u.user_id = f.friend_id);
```

### Пользовательские роли
Пользователи равноправны. Регистрация происходит по обязательным полям email и login.

### Функциональные возможности
**Фильмы**
* добавление/обновление фильма
* указание для фильма жанра(жанров) и рейтинга MPA
* получение списка всех фильмов
* получение фильма по его уникальному идентификатору (id)

**Пользователи**
* добавление/обновление пользователя
* получение списка всех пользователей
* получение пользователя по его id

**Лайки**
* добавление/удаление пользователем лайка к фильму
(каждый пользователь может поставить лайк фильму один раз)
* получение указанного количества наиболее популярных фильмов по количеству лайков (по умолчанию - 10 фильмов)

**Односторонняя дружба между пользователями (по принципу подписки)**
* приглашение другого пользоателя в друзья и удаление его из списка своих друзей
  (согласие на дружбу с другим пользователем не требуется;
  приглашение в друзья означает, что приглашенный становится другом пригласившего, но не наоборот)
* получение списка друзей пользователя по его id
* получение списка общих друзей двух пользователей

**Жанры**
* получение списка всех жанров
* получение жанра по его id

**Рейтинг MPA**
* получение списка всех видов рейтинга MPA
* получение рейтинга MPA по его id

## Технологии в проекте
* Java v.11.0.16
* Spring Boot
* Postgres
* REST API
