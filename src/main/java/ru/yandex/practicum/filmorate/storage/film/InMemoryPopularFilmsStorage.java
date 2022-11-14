package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.PopularFilmsStorage;

import java.util.*;

@Component
@Qualifier("InMemoryPopularFilmsStorage")
public class InMemoryPopularFilmsStorage implements PopularFilmsStorage {
    private final TreeSet<Film> popularFilms = new TreeSet<>();

    @Override
    public void add(Film film) { popularFilms.add(film); }

    @Override
    public void delete(Film film) { popularFilms.remove(film); }

    @Override
    public List<Film> findPopularFilms(Integer numberFilms) {
        List<Film> result = new ArrayList<>();
        Iterator itr = popularFilms.iterator();
        int i = 0;
        while (i++ != numberFilms && itr.hasNext()) { result.add((Film)itr.next()); }
        return result;
    }
}
