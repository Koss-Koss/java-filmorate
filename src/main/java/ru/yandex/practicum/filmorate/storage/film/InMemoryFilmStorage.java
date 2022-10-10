package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.ObjectStorage;

import java.util.Map;

@Component
@Qualifier("FilmStorage")
public class InMemoryFilmStorage extends ObjectStorage<Film> {
    private final Map<Integer, Film> films = getMap();
}
