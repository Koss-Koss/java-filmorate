package ru.yandex.practicum.filmorate.service.validation;

import ru.yandex.practicum.filmorate.model.User;

public interface LikeValidationService {

    void validateIsLike(Integer filmId, User user);

    void validateNotLike(Integer filmId, User user);
}
