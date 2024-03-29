package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface PopularFilmsStorage {

    void add(Film film);

    void delete(Film film);

    List<Film> findPopularFilms(Integer numberFilms);
}
