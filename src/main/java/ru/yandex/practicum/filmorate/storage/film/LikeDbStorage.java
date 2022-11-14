package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.LikesDao;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.StorageOfLists;

import java.util.List;

@Component
@Qualifier("LikeDbStorage")
public class LikeDbStorage implements StorageOfLists<Film, User> {
    private final LikesDao likesDao;

    public LikeDbStorage(LikesDao likesDao) { this.likesDao = likesDao; }

    @Override
    public void add(Film film, User user) { likesDao.save(film, user.getId()); }

    @Override
    public void delete(Film film, User user) { likesDao.delete(film, user.getId()); };

    @Override
    public List<User> getById(Integer filmId) { return likesDao.findLikesById(filmId); };
}
