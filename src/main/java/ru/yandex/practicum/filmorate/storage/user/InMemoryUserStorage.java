package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.Storage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Qualifier("UserStorage")
public class InMemoryUserStorage implements Storage<User> {
    private Map<Integer, User> users = new HashMap<>();
    private Integer currentId = 1;

    @Override
    public List<User> getAllList() { return new ArrayList<>(users.values()); }

    @Override
    public User getById(Integer id) { return users.get(id); }

    @Override
    public User add(User user) {
        user.setId(currentId);
        users.put(currentId++, user);
        return user;
    }

    @Override
    public User edit(Integer id, User user) {
        users.put(id, user);
        return user;
    }
}
