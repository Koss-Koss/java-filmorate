package ru.yandex.practicum.filmorate.service.validation.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.controller.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.dao.UserDao;
import ru.yandex.practicum.filmorate.service.validation.ContainsUserIdValidationService;

@Component
@Qualifier("ContainsUserIdDbValidationService")
public class ContainsUserIdDbValidationService implements ContainsUserIdValidationService {
    private final UserDao userDao;

    public ContainsUserIdDbValidationService(UserDao userDao) {
        this.userDao = userDao;
    }

    public void validateUserId(int id) {
        if (!userDao.isContainsId(id)) {
            throw new UserNotFoundException("При запросе по id должен существовать пользователь с указанным id");
        }
    }
}
