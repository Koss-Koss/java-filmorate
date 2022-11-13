package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.controller.exception.MPANotFoundException;
import ru.yandex.practicum.filmorate.controller.exception.MPAValidationException;
import ru.yandex.practicum.filmorate.dao.MPADao;

@Component
public class MPAValidationService {
    private final MPADao mpaDao;

    public MPAValidationService(MPADao mpaDao) {
        this.mpaDao = mpaDao;
    }

    protected void validateById(int id) {
        if ( id < 1 || !mpaDao.isContainsId(id)) {
            throw new MPANotFoundException("При запросе по id должен существовать рейтинг MPA с указанным id");
        }
        if (mpaDao.findById(id).isEmpty()) {
            throw new MPAValidationException("В запросе не может быть указан рейтинг MPA равный null");
        }
    }
}