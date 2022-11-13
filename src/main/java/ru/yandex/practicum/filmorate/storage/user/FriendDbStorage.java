package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.FriendsDao;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.StorageOfLists;

import java.util.List;

@Component
@Qualifier("FriendDbStorage")
public class FriendDbStorage implements StorageOfLists<User, User> {
    private final FriendsDao friendsDao;

    public FriendDbStorage(FriendsDao friendsDao) {
        this.friendsDao = friendsDao;
    }

    @Override
    public void add(User user, User friend) {
        friendsDao.save(user, friend.getId());
    };

    @Override
    public void delete(User user, User friend) { friendsDao.delete(user, friend.getId());
    };

    @Override
    public List<User> getById(Integer id) {
        return friendsDao.findFriendsById(id);
    };
}
