package ru.yandex.practicum.filmorate.service.validation.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.controller.exception.UserValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.validation.LikeValidationService;
import ru.yandex.practicum.filmorate.storage.StorageOfLists;

@Component
@Qualifier("LikeInMemoryValidationService")
public class LikeInMemoryValidationService implements LikeValidationService {

    private final StorageOfLists likeStorage;

    public LikeInMemoryValidationService(@Qualifier("LikeStorage") StorageOfLists likeStorage) {
        this.likeStorage = likeStorage;
    }

    @Override
    public void validateIsLike(Integer filmId, User user) {
        if (likeStorage.getById(filmId).contains(user)) {
            throw new UserValidationException("id: Попытка повторного добавления лайка пользователя id=" +
                    user.getId() + " к фильму id=" + filmId);
        }
    }

    @Override
    public void validateNotLike(Integer filmId, User user) {
        if (!likeStorage.getById(filmId).contains(user)) {
            throw new UserValidationException("id: Попытка удаления отсутствующего лайка пользователя id=" +
                    user.getId() + " к фильму id=" + filmId);
        }
    }
}
