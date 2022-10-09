package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@Component
public class InMemoryPopularFilmsStorage implements PopularFilmsStorage {
    private final TreeSet<Film> popularFilms = new TreeSet<>();

    @Override
    public void add(Film film) {
        popularFilms.add(film); }

    @Override
    public void delete(Film film) { popularFilms.remove(film); }

    @Override
    public boolean containsFilm(Film film) { return popularFilms.contains(film); }

    @Override
    public List<Film> findPopularFilms(Integer numberFilms) {
        List<Film> result = new ArrayList<>();
        Iterator itr = popularFilms.iterator();
        int i = 0;
        while (i++ != numberFilms && itr.hasNext()) { result.add((Film)itr.next()); }
        return result;
    }
}
