package ru.yandex.practicum.filmorate.storage;

import java.util.List;

public interface Storage<T> {

    List<T> getAllList();

    T getById(Integer id);

    T add(T obj);

    T edit(Integer id, T obj);
}
