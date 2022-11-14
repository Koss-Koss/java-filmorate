package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.controller.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.controller.exception.UserValidationException;
import ru.yandex.practicum.filmorate.model.User;

@Component
public class UserValidationService {

    protected void validateUserLogin(User user) {
        if (user.getLogin().contains(" ")) {
            throw new UserValidationException("login: login не может содержать пробелы");
        }
    }

    protected void validateUserId(Integer id) {
        if (id == null) {
            throw new UserValidationException("id: id пользователя - обязательное поле");
        }
        if (id <= 0) {
            throw new UserNotFoundException("id пользователя не может быть отрицательным или равным нулю");
        }
    }

    protected void validateAutoFriend(Integer userId, Integer friendId) {
        if (userId == friendId) {
            throw new UserValidationException("id: Попытка добавления в друзья пользователю id=" +
                    userId + " самого себя");
        }
    }
}
