package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.controller.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.controller.exception.UserValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.Storage;
import ru.yandex.practicum.filmorate.storage.StorageOfSets;

@Component
public class UserValidationService {

    private final Storage userStorage;
    private final StorageOfSets friendStorage;

    public UserValidationService(
            @Qualifier("UserStorage") Storage userStorage,
            @Qualifier("FriendStorage") StorageOfSets friendStorage) {
        this.userStorage = userStorage;
        this.friendStorage = friendStorage;
    }

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
        if (!userStorage.getAll().containsKey(id)) {
            throw new UserNotFoundException("При запросе по id должен существовать пользователь с указанным id");
        }
    }

    protected void validateIsFriend(Integer userId, User user) {
        if (friendStorage.getById(userId).contains(user)) {
            throw new UserValidationException("id: Попытка повторного добавления пользователю id=" +
                    userId + " друга id=" + user.getId());
        }
    }

    protected void validateNotFriend(Integer userId, User user) {
        if (!friendStorage.getById(userId).contains(user)) {
            throw new UserValidationException("id: Попытка удаления у пользователя id=" +
                    userId + " отсутствующего друга id=" + user.getId());
        }
    }

    protected void validateAutoFriend(Integer userId, Integer friendId) {
        if (userId == friendId) {
            throw new UserValidationException("id: Попытка добавления в друзья пользователю id=" +
                    userId + " самого себя");
        }
    }
}
