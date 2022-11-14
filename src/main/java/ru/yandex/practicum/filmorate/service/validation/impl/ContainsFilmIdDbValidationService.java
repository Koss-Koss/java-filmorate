package ru.yandex.practicum.filmorate.service.validation.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.controller.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.service.validation.ContainsFilmIdValidationService;

@Component
@Qualifier("ContainsFilmIdDbValidationService")
public class ContainsFilmIdDbValidationService implements ContainsFilmIdValidationService {
    private final FilmDao filmDao;

    public ContainsFilmIdDbValidationService(FilmDao filmDao) {
        this.filmDao = filmDao;
    }

    public void validateFilmId(int id) {
        if (!filmDao.isContainsId(id)) {
            throw new UserNotFoundException("При запросе по id должен существовать фильм с указанным id");
        }
    }
}
