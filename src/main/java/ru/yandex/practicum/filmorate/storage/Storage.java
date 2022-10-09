package ru.yandex.practicum.filmorate.storage;

import java.util.List;
import java.util.Map;

public interface Storage<T> {

    Integer getCurrentId();

    Map<Integer, T> getAll();

    List<T> getAllList();

    T getById(Integer id);

    void add(T obj);

    void edit(Integer id, T obj);
}
