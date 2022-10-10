package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.ObjectStorage;

import java.util.Map;

@Component
@Qualifier("UserStorage")
public class InMemoryUserStorage extends ObjectStorage {
    private final Map<Integer, User> users = getMap();
}
