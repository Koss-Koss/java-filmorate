package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.controller.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {
    private final Map<Integer, Film> films = new HashMap<>();
    private int currentFilmId = 1;

    @GetMapping()
    public List<Film> findAll() {
        return new ArrayList<>(films.values());
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping()
    public Film create(@Valid @RequestBody Film film) {
        validateReleaseDate(film);
        film.setId(currentFilmId);
        films.put(currentFilmId, film);
        log.info("Добавлен фильм: {}", film);
        return films.get(currentFilmId++);
    }

    @PutMapping()
    public Film update(@Valid @RequestBody Film film) {
        validateId(film);
        validateReleaseDate(film);
        films.put(film.getId(), film);
        log.info("Отредактирован фильм: {}", film);
        return films.get(film.getId());
    }

    private void validateReleaseDate(Film film) {
        LocalDate releaseDate = film.getReleaseDate();
        if (film.getReleaseDate() == null) { return; }
        if (releaseDate != null && releaseDate.isBefore(LocalDate.parse("1895-12-28"))) {
            String message = "Дата релиза фильма не может быть ранее 28.12.1895";
            log.warn(message);
            throw new ValidationException(message);
        }
    }

    private void validateId(Film film) {
        Integer id = film.getId();
        if (id == null) {
            String message = "id фильма - обязательное поле";
            log.warn(message);
            throw new ValidationException(message);
        }
        if (id <= 0) {
            String message = "id фильма не может быть отрицательным или равным нулю";
            log.warn(message);
            throw new ValidationException(message);
        }
        if (!films.containsKey(id)) {
            String message = "При PUT-запросе должен существовать фильм с указанным id";
            log.warn(message);
            throw new ValidationException(message);
        }
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<String> handleException(ValidationException exception) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(exception.getMessage());
    }

}
