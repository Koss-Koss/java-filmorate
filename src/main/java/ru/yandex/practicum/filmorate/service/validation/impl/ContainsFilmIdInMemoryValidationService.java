package ru.yandex.practicum.filmorate.service.validation.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.controller.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.validation.ContainsFilmIdValidationService;
import ru.yandex.practicum.filmorate.storage.Storage;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Qualifier("ContainsFilmIdInMemoryValidationService")
public class ContainsFilmIdInMemoryValidationService implements ContainsFilmIdValidationService {

    private final Storage<Film> filmStorage;

    public ContainsFilmIdInMemoryValidationService(
            @Qualifier("FilmStorage") Storage filmStorage) { this.filmStorage = filmStorage; }

    public void validateFilmId(int id) {
        List<Integer> filmId = filmStorage.getAllList().stream().map(Film::getId).collect(Collectors.toList());
        if (!filmId.contains(id)) {
            throw new UserNotFoundException("При запросе по id должен существовать фильм с указанным id");
        }
    }
}
