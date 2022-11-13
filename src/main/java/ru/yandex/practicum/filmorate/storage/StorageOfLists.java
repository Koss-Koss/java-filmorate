package ru.yandex.practicum.filmorate.storage;

import java.util.List;

public interface StorageOfLists<F, T> {

    void add(F obj1, T obj2);

    void delete(F obj1, T obj2);

    List<T> getById(Integer id);
}
