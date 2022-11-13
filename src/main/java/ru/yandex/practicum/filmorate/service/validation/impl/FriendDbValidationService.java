package ru.yandex.practicum.filmorate.service.validation.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.controller.exception.FriendValidationException;
import ru.yandex.practicum.filmorate.controller.exception.UserValidationException;
import ru.yandex.practicum.filmorate.dao.FriendsDao;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.validation.FriendValidationService;

@Component
@Qualifier("FriendDbValidationService")
public class FriendDbValidationService implements FriendValidationService {
    private final FriendsDao friendsDao;

    public FriendDbValidationService(FriendsDao friendsDao) {
        this.friendsDao = friendsDao;
    }

    @Override
    public void validateIsFriend(Integer userId, User user) {
        if (friendsDao.isContainsIdFriendId(userId, user.getId()))
            throw new FriendValidationException("id: Попытка повторного добавления пользователю id=" +
                    user.getId() + " в друзья пользователя id=" + userId);
    };

    @Override
    public void validateNotFriend(Integer userId, User user) {
        if (!friendsDao.isContainsIdFriendId(userId, user.getId())) {
            throw new UserValidationException("id: Попытка удаления у пользователя id=" +
                    userId + " отсутствующего друга id=" + user.getId());
        }
    };
}
