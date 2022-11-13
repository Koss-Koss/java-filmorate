package ru.yandex.practicum.filmorate.service.validation;

import ru.yandex.practicum.filmorate.model.User;

public interface FriendValidationService {

    void validateIsFriend(Integer userId, User user);

    void validateNotFriend(Integer userId, User user);
}
