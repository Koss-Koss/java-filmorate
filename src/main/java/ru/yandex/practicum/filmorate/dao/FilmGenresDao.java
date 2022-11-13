package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface FilmGenresDao {

    List<Genre> findById(int id);

    void addById(int id, List<Genre> genres);

    void deleteById(int id);
}
