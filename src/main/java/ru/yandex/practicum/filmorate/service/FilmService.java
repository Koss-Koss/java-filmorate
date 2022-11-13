package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.validation.ContainsFilmIdValidationService;
import ru.yandex.practicum.filmorate.service.validation.ContainsUserIdValidationService;
import ru.yandex.practicum.filmorate.service.validation.LikeValidationService;
import ru.yandex.practicum.filmorate.storage.Storage;
import ru.yandex.practicum.filmorate.storage.StorageOfLists;
import ru.yandex.practicum.filmorate.storage.PopularFilmsStorage;

import java.util.List;

@Service
@Slf4j
public class FilmService {
    private final Storage<Film> filmStorage;
    private final Storage<User> userStorage;
    private final StorageOfLists likeStorage;
    private final PopularFilmsStorage popularFilmsStorage;
    private final FilmValidationService filmValidationService;
    private final GenreValidationService genreValidationService;
    private final UserValidationService userValidationService;
    private final ContainsFilmIdValidationService containsFilmIdService;
    private final ContainsUserIdValidationService containsUserIdService;
    private final MPAValidationService mpaValidationService;
    private final LikeValidationService likeValidationService;

    public FilmService(
            @Qualifier("FilmDbStorage") Storage<Film> filmStorage,
            @Qualifier("UserDbStorage") Storage<User> userStorage,
            @Qualifier("LikeDbStorage") StorageOfLists likeStorage,
            @Qualifier("PopularFilmsDbStorage") PopularFilmsStorage popularFilmsStorage,
            FilmValidationService filmValidationService,
            GenreValidationService genreValidationService,
            UserValidationService userValidationService,
            @Qualifier("ContainsFilmIdDbValidationService")
                ContainsFilmIdValidationService containsFilmIdService,
            @Qualifier("ContainsUserIdDbValidationService")
                ContainsUserIdValidationService containsUserIdService,
            MPAValidationService mpaValidationService,
            @Qualifier("LikeDbValidationService")
                LikeValidationService likeValidationService) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.likeStorage = likeStorage;
        this.popularFilmsStorage = popularFilmsStorage;
        this.filmValidationService = filmValidationService;
        this.genreValidationService = genreValidationService;
        this.userValidationService = userValidationService;
        this.containsFilmIdService = containsFilmIdService;
        this.containsUserIdService = containsUserIdService;
        this.mpaValidationService = mpaValidationService;
        this.likeValidationService = likeValidationService;
    }

    public List<Film> findAll() {
        return filmStorage.getAllList();
    }

    public Film findFilm(Integer filmId) {
        filmValidationService.validateFilmId(filmId);
        containsFilmIdService.validateFilmId(filmId);
        return filmStorage.getById(filmId);
    }

    public Film create(Film film) {
        genreValidationService.validateDuplicatesOfGenres(film);
        filmValidationService.validateFilmReleaseDate(film);
        mpaValidationService.validateById(film.getMpa().get().getId());
        film.setNumberOfLikes(0);
        filmStorage.add(film);
        popularFilmsStorage.add(film);
        log.info("Добавлен фильм: {}", film);
        return film;
    }

    public Film update(Film film) {
        genreValidationService.validateDuplicatesOfGenres(film);
        Integer filmId = film.getId();
        filmValidationService.validateFilmId(filmId);
        filmValidationService.validateFilmReleaseDate(film);
        popularFilmsStorage.delete(film);
        mpaValidationService.validateById(film.getMpa().get().getId());
        film.setNumberOfLikes(likeStorage.getById(filmId).size());
        filmStorage.edit(film.getId(), film);
        popularFilmsStorage.add(film);
        log.info("Отредактирован фильм: {}", film);
        return film;
    }

    public void addLike(Integer filmId, Integer userId) {
        Film film = filmStorage.getById(filmId);
        User user = userStorage.getById(userId);
        filmValidationService.validateFilmId(filmId);
        containsFilmIdService.validateFilmId(filmId);
        userValidationService.validateUserId(userId);
        containsUserIdService.validateUserId(userId);
        likeValidationService.validateIsLike(filmId, user);
        popularFilmsStorage.delete(film);
        likeStorage.add(film, user);
        filmStorage.edit(filmId, film);
        popularFilmsStorage.add(film);
        log.info("Пользователь id={} поставил лайк фильму id={}", userId, filmId);
    }

    public void deleteLike(Integer filmId, Integer userId) {
        Film film = filmStorage.getById(filmId);
        User user = userStorage.getById(userId);
        filmValidationService.validateFilmId(filmId);
        userValidationService.validateUserId(userId);
        likeValidationService.validateNotLike(filmId, user);
        popularFilmsStorage.delete(film);
        likeStorage.delete(film, user);
        filmStorage.edit(filmId, film);
        popularFilmsStorage.add(film);
        log.info("Пользователь id={} удалил лайк фильму id={}", userId, filmId);
    }

    public List<Film> findPopularFilms(Integer numberOfFilms) {
        return popularFilmsStorage.findPopularFilms(numberOfFilms);
    }
}
