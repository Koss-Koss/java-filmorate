package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorageOfSets;

import java.util.Map;
import java.util.Set;

@Component
@Qualifier("LikeStorage")
public class InMemoryLikeStorage extends UserStorageOfSets {
    private final Map<Integer, Set<User>> likes = getMap();
}
