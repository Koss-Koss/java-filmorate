package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.PopularFilmsStorage;

import java.util.List;

@Component
@Qualifier("PopularFilmsDbStorage")
public class PopularFilmsDbStorage implements PopularFilmsStorage {
    private final FilmDao filmDao;

    public PopularFilmsDbStorage(FilmDao filmDao) { this.filmDao = filmDao; }

    @Override
    public void add(Film film) {};

    @Override
    public void delete(Film film) {};

    @Override
    public List<Film> findPopularFilms(Integer numberFilms) {
        return filmDao.findPopularFilms(numberFilms);
    };
}
