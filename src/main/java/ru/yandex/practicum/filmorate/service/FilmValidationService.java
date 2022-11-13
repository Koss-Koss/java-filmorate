package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.controller.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.controller.exception.FilmValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.time.Month;

@Component
public class FilmValidationService {
    public static final LocalDate BEGINNING_OF_CINEMA_ERA = LocalDate.of(1895, Month.DECEMBER, 28);

    protected void validateFilmReleaseDate(Film film) {
        LocalDate releaseDate = film.getReleaseDate();
        if (releaseDate != null && releaseDate.isBefore(BEGINNING_OF_CINEMA_ERA)) {
            throw new FilmValidationException("releaseDate: Дата релиза фильма не может быть ранее 28.12.1895");
        }
    }

    protected void validateFilmId(Integer id) {
        if (id == null) {
            throw new FilmValidationException("id: id фильма - обязательное поле");
        }
        if (id <= 0) {
            throw new FilmNotFoundException("id фильма не может быть отрицательным или равным нулю");
        }
    }
}
