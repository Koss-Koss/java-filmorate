package ru.yandex.practicum.filmorate.storage;

import java.util.Set;

public interface StorageOfSets<T> {

    void add(Integer id, T obj);

    void delete(Integer id, T obj);

    Set<T> getById(Integer id);
}
