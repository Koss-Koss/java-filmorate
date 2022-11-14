package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.Storage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Qualifier("FilmStorage")
public class InMemoryFilmStorage implements Storage<Film> {
    private Map<Integer, Film> objects = new HashMap<>();
    private Integer currentId = 1;

    @Override
    public List<Film> getAllList() { return new ArrayList<>(objects.values()); }

    @Override
    public Film getById(Integer id) { return objects.get(id); }

    @Override
    public Film add(Film film) {
        film.setId(currentId);
        objects.put(currentId++, film);
        return film;
    }

    @Override
    public Film edit(Integer id, Film film) {
        objects.put(id, film);
        return film;
    }
}
