package ru.yandex.practicum.filmorate.service.validation.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.controller.exception.UserValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.validation.FriendValidationService;
import ru.yandex.practicum.filmorate.storage.StorageOfLists;

@Component
@Qualifier("FriendInMemoryValidateService")
public class FriendInMemoryValidateService implements FriendValidationService {
    private final StorageOfLists<User, User> friendStorage;

    public FriendInMemoryValidateService(@Qualifier("FriendStorage") StorageOfLists<User, User> friendStorage) {
        this.friendStorage = friendStorage;
    }

    @Override
    public void validateIsFriend(Integer userId, User user) {
        if (friendStorage.getById(userId).contains(user)) {
            throw new UserValidationException("id: Попытка повторного добавления пользователю id=" +
                    userId + " друга id=" + user.getId());
        }
    };

    @Override
    public void validateNotFriend(Integer userId, User user) {
        if (!friendStorage.getById(userId).contains(user)) {
            throw new UserValidationException("id: Попытка удаления у пользователя id=" +
                    userId + " отсутствующего друга id=" + user.getId());
        }
    };
}
