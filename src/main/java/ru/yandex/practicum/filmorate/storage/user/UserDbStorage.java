package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.UserDao;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.Storage;

import java.util.List;
import java.util.Optional;

@Component
@Qualifier("UserDbStorage")
public class UserDbStorage implements Storage<User> {
    private final UserDao userDao;

    public UserDbStorage(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public User getById(Integer id) {
        Optional<User> user = userDao.findById(id);
        if (user.isEmpty()) { return null; }
        return user.get();
    }

    @Override
    public List<User> getAllList() {
        return userDao.findAll();
    }

    @Override
    public User add(User user) {
        return userDao.save(user);
    }

    @Override
    public User edit(Integer id, User user) {
        return userDao.update(id, user);
    }
}
