package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

public abstract class UserStorageOfSets implements StorageOfSets<User> {
    private Map<Integer, Set<User>> objects = new HashMap<>();

    protected Map<Integer, Set<User>> getMap() { return objects; }

    @Override
    public void add(Integer id, User user) {
        if (!objects.containsKey(id)) {
            objects.put(id, new HashSet<>(Arrays.asList()));
        }
        objects.get(id).add(user);
    }

    @Override
    public void delete(Integer id, User user) {
        if (objects.containsKey(id)) {
            objects.get(id).remove(user);
        }
    }

    @Override
    public Set<User> getById(Integer id) {
        if (objects.get(id) == null) { objects.put(id, new HashSet<>(Arrays.asList())); }
        return objects.get(id);
    }
}
