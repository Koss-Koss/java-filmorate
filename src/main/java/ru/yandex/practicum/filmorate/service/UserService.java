package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.Storage;
import ru.yandex.practicum.filmorate.storage.StorageOfSets;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class UserService {

    private final Storage<User> userStorage;
    private final StorageOfSets friendStorage;
    private final UserValidationService userValidationService;

    public UserService(
            @Qualifier("UserStorage") Storage<User> userStorage,
            @Qualifier("FriendStorage") StorageOfSets friendStorage,
            UserValidationService userValidationService) {
        this.userStorage = userStorage;
        this.friendStorage = friendStorage;
        this.userValidationService = userValidationService;
    }

    public List<User> findAll() {
        return userStorage.getAllList();
    }

    public User findUser(Integer userId) {
        userValidationService.validateUserId(userId);
        return userStorage.getById(userId);
    }

    public User create(User user) {
        userValidationService.validateUserLogin(user);
        String name = user.getName();
        if (name == null || name.equals("")) { user.setName(user.getLogin()); }
        user.setId(userStorage.getCurrentId());
        userStorage.add(user);
        log.info("Добавлен пользователь: {}", user);
        return user;
    }

    public User update(User user) {
        userValidationService.validateUserId(user.getId());
        userValidationService.validateUserLogin(user);
        String name = user.getName();
        if (name == null || name.equals("")) { user.setName(user.getLogin()); }
        userStorage.edit(user.getId(), user);
        log.info("Отредактирован пользователь: {}", user);
        return user;
    }

    public void addFriend(Integer userId, Integer friendId) {
        User user = userStorage.getById(userId);
        User friend = userStorage.getById(friendId);
        userValidationService.validateUserId(userId);
        userValidationService.validateUserId(friendId);
        userValidationService.validateIsFriend(userId, friend);
        userValidationService.validateIsFriend(friendId, user);
        userValidationService.validateAutoFriend(userId, friendId);
        friendStorage.add(userId, friend);
        friendStorage.add(friendId, user);
        log.info("Пользователи id={} и id={} добавлены в друзья друг к другу", userId, friendId);
    }

    public void deleteFriend(Integer userId, Integer friendId) {
        User user = userStorage.getById(userId);
        User friend = userStorage.getById(friendId);
        userValidationService.validateUserId(userId);
        userValidationService.validateUserId(friendId);
        userValidationService.validateNotFriend(userId, friend);
        userValidationService.validateNotFriend(friendId, user);
        friendStorage.delete(userId, friend);
        friendStorage.delete(friendId, user);
        log.info("Пользователи id={} и id={} удалены из друзей друг друга", userId, friendId);
    }

    public Set<User> findFriendsById(Integer userId) {
        userValidationService.validateUserId(userId);
        return friendStorage.getById(userId);
    }

    public Set<User> findMutualFriends(Integer userId, Integer otherUserId) {
        userValidationService.validateUserId(userId);
        userValidationService.validateUserId(otherUserId);
        Set<User> mutualFriends = new HashSet<>(friendStorage.getById(userId));
        mutualFriends.retainAll(friendStorage.getById(otherUserId));
        return mutualFriends;
    }

}
