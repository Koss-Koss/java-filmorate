package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.Storage;
import ru.yandex.practicum.filmorate.storage.StorageOfSets;
import ru.yandex.practicum.filmorate.storage.film.PopularFilmsStorage;

import java.util.List;

@Service
@Slf4j
public class FilmService {

    private final Storage filmStorage;
    private final Storage userStorage;
    private final StorageOfSets likeStorage;
    private final PopularFilmsStorage popularFilmsStorage;
    private final FilmValidationService filmValidationService;
    private final UserValidationService userValidationService;

    public FilmService(
            @Qualifier("FilmStorage") Storage filmStorage,
            @Qualifier("UserStorage") Storage userStorage,
            @Qualifier("LikeStorage") StorageOfSets likeStorage,
            PopularFilmsStorage popularFilmsStorage,
            FilmValidationService filmValidationService,
            UserValidationService userValidationService) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.likeStorage = likeStorage;
        this.popularFilmsStorage = popularFilmsStorage;
        this.filmValidationService = filmValidationService;
        this.userValidationService = userValidationService;
    }

    public List<Film> findAll() {
        return filmStorage.getAllList();
    }

    public Film findFilm(Integer filmId) {
        filmValidationService.validateFilmId(filmId);
        return (Film)filmStorage.getById(filmId);
    }

    public Film create(Film film) {
        filmValidationService.validateFilmReleaseDate(film);
        film.setId(filmStorage.getCurrentId());
        film.setNumberOfLikes(0);
        filmStorage.add(film);
        popularFilmsStorage.add(film);
        log.info("Добавлен фильм: {}", film);
        return film;
    }

    public Film update(Film film) {
        Integer filmId = film.getId();
        filmValidationService.validateFilmId(filmId);
        filmValidationService.validateFilmReleaseDate(film);
        popularFilmsStorage.delete(film);
        film.setNumberOfLikes(likeStorage.getById(filmId).size());
        filmStorage.edit(film.getId(), film);
        popularFilmsStorage.add(film);
        log.info("Отредактирован фильм: {}", film);
        return film;
    }

    public void addLike(Integer filmId, Integer userId) {
        Film film = (Film)filmStorage.getById(filmId);
        User user = (User)userStorage.getById(userId);
        filmValidationService.validateFilmId(filmId);
        userValidationService.validateUserId(userId);
        filmValidationService.validateIsLike(filmId, user);
        popularFilmsStorage.delete(film);
        likeStorage.add(filmId, user);
        film.setNumberOfLikes(likeStorage.getById(filmId).size());
        popularFilmsStorage.add(film);
        log.info("Пользователь id={} поставил лайк фильму id={}", userId, filmId);
    }

    public void deleteLike(Integer filmId, Integer userId) {
        Film film = (Film)filmStorage.getById(filmId);
        User user = (User)userStorage.getById(userId);
        filmValidationService.validateFilmId(filmId);
        userValidationService.validateUserId(userId);
        filmValidationService.validateNotLike(filmId, user);
        popularFilmsStorage.delete(film);
        likeStorage.delete(filmId, user);
        film.setNumberOfLikes(likeStorage.getById(filmId).size());
        popularFilmsStorage.add(film);
        log.info("Пользователь id={} удалил лайк фильму id={}", userId, filmId);
    }

    public List<Film> findPopularFilms(Integer numberOfFilms) {
        return popularFilmsStorage.findPopularFilms(numberOfFilms);
    }

}
