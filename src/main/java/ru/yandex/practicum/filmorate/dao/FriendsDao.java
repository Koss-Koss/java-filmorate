package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface FriendsDao {

    boolean isContainsIdFriendId(int id, int friendId);

    void save(User user, int friendId);

    void delete(User user, int friendId);

    List<User> findFriendsById(int id);
}
