package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface LikesDao {

    boolean isContainsIdUserId(int filmId, int userId);
    void save(Film film, int userId);

    void delete(Film film, int userId);

    List<User> findLikesById(int filmId);
}
