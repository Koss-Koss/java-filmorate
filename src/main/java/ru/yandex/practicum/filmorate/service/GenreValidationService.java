package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.controller.exception.GenreNotFoundException;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.*;

@Component
@Slf4j
public class GenreValidationService {
    private final GenreDao genreDao;

    public GenreValidationService(GenreDao genreDao) {
        this.genreDao = genreDao;
    }

    protected void validateById(Optional<Genre> genre) {
        if (genre.isEmpty()) {
            throw new GenreNotFoundException("При запросе по id должен существовать жанр с указанным id");
        }
    }

    protected void validateDuplicatesOfGenres(Film film) {
        List<Genre> genres = new ArrayList<>();
        if (film.getGenres() == null) {
            film.setGenres(genres);
        } else {
            Set<Genre> uniqueGenres = new LinkedHashSet<>(film.getGenres());
            if (uniqueGenres.size() < film.getGenres().size()) {
                genres.addAll(uniqueGenres);
                film.setGenres(genres);
                log.info("Удалены дубликаты id жанров из запроса");
            }
        }
    }
}
