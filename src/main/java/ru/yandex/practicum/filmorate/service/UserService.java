package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.validation.ContainsUserIdValidationService;
import ru.yandex.practicum.filmorate.service.validation.FriendValidationService;
import ru.yandex.practicum.filmorate.storage.Storage;
import ru.yandex.practicum.filmorate.storage.StorageOfLists;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class UserService {
    private final Storage<User> userStorage;
    private final StorageOfLists friendStorage;
    private final UserValidationService userValidationService;
    private final ContainsUserIdValidationService containsUserIdValidationService;
    private final FriendValidationService friendValidationService;

    public UserService(
            @Qualifier("UserDbStorage") Storage<User> userStorage,
            @Qualifier("FriendDbStorage") StorageOfLists friendStorage,
            UserValidationService userValidationService,
            @Qualifier("ContainsUserIdDbValidationService")
                ContainsUserIdValidationService containsUserIdValidationService,
            @Qualifier("FriendDbValidationService")
            FriendValidationService friendValidationService) {
        this.userStorage = userStorage;
        this.friendStorage = friendStorage;
        this.userValidationService = userValidationService;
        this.containsUserIdValidationService = containsUserIdValidationService;
        this.friendValidationService = friendValidationService;
    }

    public List<User> findAll() {
        return userStorage.getAllList();
    }

    public User findUser(Integer userId) {
        userValidationService.validateUserId(userId);
        containsUserIdValidationService.validateUserId(userId);
        return userStorage.getById(userId);
    }

    public User create(User user) {
        userValidationService.validateUserLogin(user);
        String name = user.getName();
        if (name == null || name.equals("")) { user.setName(user.getLogin()); }
        userStorage.add(user);
        log.info("Добавлен пользователь: {}", user);
        return user;
    }

    public User update(User user) {
        userValidationService.validateUserId(user.getId());
        userValidationService.validateUserLogin(user);
        containsUserIdValidationService.validateUserId(user.getId());
        String name = user.getName();
        if (name == null || name.equals("")) { user.setName(user.getLogin()); }
        userStorage.edit(user.getId(), user);
        log.info("Отредактирован пользователь: {}", user);
        return user;
    }

    public void addFriend(Integer userId, Integer friendId) {
        User friend = userStorage.getById(friendId);
        userValidationService.validateUserId(userId);
        userValidationService.validateUserId(friendId);
        containsUserIdValidationService.validateUserId(userId);
        containsUserIdValidationService.validateUserId(friendId);
        friendValidationService.validateIsFriend(userId, friend);
        userValidationService.validateAutoFriend(userId, friendId);
        friendStorage.add(userStorage.getById(userId), friend);
        log.info("Пользователь id={} добавлен в друзья к пользователю id={}", friendId, userId);
    }

    public void deleteFriend(Integer userId, Integer friendId) {
        User friend = userStorage.getById(friendId);
        userValidationService.validateUserId(userId);
        userValidationService.validateUserId(friendId);
        containsUserIdValidationService.validateUserId(userId);
        containsUserIdValidationService.validateUserId(friendId);
        friendValidationService.validateNotFriend(userId, friend);
        friendStorage.delete(userStorage.getById(userId), friend);
        log.info("Пользователь id={} удален из друзей пользователя id={}", friendId, userId);
    }

    public List<User> findFriendsById(Integer userId) {
        userValidationService.validateUserId(userId);
        containsUserIdValidationService.validateUserId(userId);
        return friendStorage.getById(userId);
    }

    public List<User> findMutualFriends(Integer userId, Integer otherUserId) {
        userValidationService.validateUserId(userId);
        userValidationService.validateUserId(otherUserId);
        containsUserIdValidationService.validateUserId(userId);
        containsUserIdValidationService.validateUserId(otherUserId);
        List<User> mutualFriends = new ArrayList<>(friendStorage.getById(userId));
        mutualFriends.retainAll(friendStorage.getById(otherUserId));
        return mutualFriends;
    }
}
