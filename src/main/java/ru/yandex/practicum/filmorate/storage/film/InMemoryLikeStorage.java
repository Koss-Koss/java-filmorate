package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.StorageOfLists;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Qualifier("LikeStorage")
public class InMemoryLikeStorage implements StorageOfLists<Film, User> {
    private Map<Integer, List<User>> objects = new HashMap<>();

    @Override
    public void add(Film film, User user) {
        int id = film.getId();
        if (!objects.containsKey(id)) {
            objects.put(id, new ArrayList<>());
        }
        objects.get(id).add(user);
    }

    @Override
    public void delete(Film film, User user) {
        int id = film.getId();
        if (objects.containsKey(id)) {
            objects.get(id).remove(user);
        }
    }

    @Override
    public List<User> getById(Integer id) {
        if (objects.get(id) == null) { objects.put(id, new ArrayList<>()); }
        return objects.get(id);
    }
}
