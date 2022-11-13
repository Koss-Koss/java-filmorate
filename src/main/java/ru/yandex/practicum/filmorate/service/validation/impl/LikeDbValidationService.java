package ru.yandex.practicum.filmorate.service.validation.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.controller.exception.LikeValidationException;
import ru.yandex.practicum.filmorate.dao.LikesDao;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.validation.LikeValidationService;

@Component
@Qualifier("LikeDbValidationService")
public class LikeDbValidationService implements LikeValidationService {

    private final LikesDao likesDao;

    public LikeDbValidationService(LikesDao likesDao) {
        this.likesDao = likesDao;
    }

    @Override
    public void validateIsLike(Integer filmId, User user) {
        if (likesDao.isContainsIdUserId(filmId, user.getId()))
            throw new LikeValidationException("id: Попытка повторного добавления лайка пользователя id=" +
                    user.getId() + " к фильму id=" + filmId);
    }

    @Override
    public void validateNotLike(Integer filmId, User user) {
        if (!likesDao.isContainsIdUserId(filmId, user.getId())) {
            throw new LikeValidationException("id: Попытка удаления отсутствующего лайка пользователя id=" +
                    user.getId() + " к фильму id=" + filmId);
        }
    }
}
