package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorageOfSets;

import java.util.Map;
import java.util.Set;

@Component
@Qualifier("FriendStorage")
public class InMemoryFriendStorage extends UserStorageOfSets {
    private final Map<Integer, Set<User>> friends = getMap();
}
