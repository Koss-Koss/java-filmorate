package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.controller.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.controller.exception.FilmValidationException;
import ru.yandex.practicum.filmorate.controller.exception.UserValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.Storage;
import ru.yandex.practicum.filmorate.storage.StorageOfSets;
import ru.yandex.practicum.filmorate.storage.film.PopularFilmsStorage;

import java.time.LocalDate;

@Component
public class FilmValidationService {
    private static final LocalDate BEGINNING_OF_CINEMA_ERA = LocalDate.parse("1895-12-28");

    private final Storage filmStorage;
    private final StorageOfSets likeStorage;
    private final PopularFilmsStorage inMemoryPopularFilmsStorage;

    public FilmValidationService(
            @Qualifier("FilmStorage")Storage filmStorage,
            @Qualifier("LikeStorage") StorageOfSets likeStorage,
            PopularFilmsStorage inMemoryPopularFilmsStorage) {
        this.filmStorage = filmStorage;
        this.likeStorage = likeStorage;
        this.inMemoryPopularFilmsStorage = inMemoryPopularFilmsStorage;
    }

    void validateFilmReleaseDate(Film film) {
        LocalDate releaseDate = film.getReleaseDate();
        if (film.getReleaseDate() == null) { return; }
        if (releaseDate != null && releaseDate.isBefore(BEGINNING_OF_CINEMA_ERA)) {
            throw new FilmValidationException("releaseDate: Дата релиза фильма не может быть ранее 28.12.1895");
        }
    }

    void validateFilmId(Integer id) {
        if (id == null) {
            throw new FilmValidationException("id: id фильма - обязательное поле");
        }
        if (id <= 0) {
            throw new FilmNotFoundException("id фильма не может быть отрицательным или равным нулю");
        }
        if (!filmStorage.getAll().containsKey(id)) {
            throw new FilmNotFoundException("При запросе по id должен существовать фильм с указанным id");
        }
    }

    void validateIsLike(Integer filmId, User user) {
        if (likeStorage.getById(filmId).contains(user)) {
            throw new UserValidationException("id: Попытка повторного добавления лайка пользователя id=" +
                    user.getId() + " к фильму id=" + filmId);
        }
    }

    void validateNotLike(Integer filmId, User user) {
        if (!likeStorage.getById(filmId).contains(user)) {
            throw new UserValidationException("id: Попытка удаления отсутствующего лайка пользователя id=" +
                    user.getId() + " к фильму id=" + filmId);
        }
    }

}
