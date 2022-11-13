package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmDao {
    Film findById(int id);

    List<Film> findAll();

    boolean isContainsId(int id);

    Film save(Film film);

    Film update(int id, Film film);

    List<Film> findPopularFilms(int numberOfFilms);
}
