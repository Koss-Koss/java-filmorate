package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.Storage;

import java.util.List;

@Component
@Qualifier("FilmDbStorage")
public class FilmDbStorage implements Storage<Film> {
    private final FilmDao filmDao;

    public FilmDbStorage(FilmDao filmDao) {
        this.filmDao = filmDao;
    }

    @Override
    public Film getById(Integer id) { return filmDao.findById(id); }

    @Override
    public List<Film> getAllList() { return filmDao.findAll(); };

    @Override
    public Film add(Film film) {
        return filmDao.save(film);
    }

    @Override
    public Film edit(Integer id, Film film) {
        return filmDao.update(id, film);
    };
}
