package ru.yandex.practicum.filmorate.storage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class ObjectStorage<T> implements Storage<T> {
    private Map<Integer, T> objects = new HashMap<>();
    private Integer currentId = 1;

    protected Map<Integer, T> getMap() { return objects; }

    @Override
    public Integer getCurrentId() { return currentId; }

    @Override
    public Map<Integer, T> getAll() {
        return objects;
    }

    @Override
    public List<T> getAllList() { return new ArrayList<>(objects.values()); }

    @Override
    public T getById(Integer id) { return objects.get(id); }

    @Override
    public void add(T obj) {
        objects.put(currentId++, obj);
    }

    @Override
    public void edit(Integer id, T obj) {
        objects.put(id, obj);
    }
}
