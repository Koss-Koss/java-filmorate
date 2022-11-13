package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserDao {
    Optional<User> findById(int id);

    List<User> findAll();

    boolean isContainsId(int id);

    User save(User user);

    User update(int id, User user);
}

