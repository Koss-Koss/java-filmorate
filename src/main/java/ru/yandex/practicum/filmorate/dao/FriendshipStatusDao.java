package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.FriendshipStatus;

import java.util.List;
import java.util.Optional;

public interface FriendshipStatusDao {
    Optional<FriendshipStatus> findById(int id);

    List<FriendshipStatus> findAll();
}
