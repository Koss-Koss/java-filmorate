package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.StorageOfLists;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Qualifier("FriendStorage")
public class InMemoryFriendStorage implements StorageOfLists<User, User> {
    private Map<Integer, List<User>> objects = new HashMap<>();

    @Override
    public void add(User user, User friend) {
        int id = user.getId();
        if (!objects.containsKey(id)) {
            objects.put(id, new ArrayList<>());
        }
        objects.get(id).add(friend);
    }

    @Override
    public void delete(User user, User friend) {
        int id = user.getId();
        if (objects.containsKey(id)) {
            objects.get(id).remove(friend);
        }
    }

    @Override
    public List<User> getById(Integer id) {
        if (objects.get(id) == null) { objects.put(id, new ArrayList<>()); }
        return objects.get(id);
    }
}
