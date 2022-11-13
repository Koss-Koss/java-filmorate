package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.MPA;

import java.util.List;
import java.util.Optional;

public interface MPADao {
    Optional<MPA> findById(int id);

    List<MPA> findAll();

    boolean isContainsId(int id);
}
