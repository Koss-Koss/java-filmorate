package ru.yandex.practicum.filmorate.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.FriendsDao;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static ru.yandex.practicum.filmorate.dao.impl.UserDaoImpl.getUserFromResultSet;

@Component
public class FriendsDaoImpl implements FriendsDao {
    private final Logger log = LoggerFactory.getLogger(UserDaoImpl.class);
    private final JdbcTemplate jdbcTemplate;
    private final int INDEX_OF_WAITING_FRIENDSHIP_STATUS = 1;
    private final int INDEX_OF_AGREE_FRIENDSHIP_STATUS = 2;

    public FriendsDaoImpl(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }
    @Override
    public boolean isContainsIdFriendId(int id, int friendId) {
        SqlRowSet userRows =
                jdbcTemplate.queryForRowSet("select * from friends where user_id = ? and friend_id = ?", id, friendId);
        if (userRows.next()) {
            log.info("В списке друзей пользователя id = {} найден пользователь id = {}", id, friendId);
            return true;
        }
        log.info("Пользователь id = {} не найден в списке друзей пользователя id = {} ", friendId, id);
        return false;
    }

    @Override
    public void save(User user, int friendId) {
        int id = user.getId();
        final String sql = "insert into friends (user_id, friend_id, friendship_status_id) values (?, ?, ?)";
        jdbcTemplate.update(sql, id, friendId, INDEX_OF_WAITING_FRIENDSHIP_STATUS);
        updateStatusAfterAddFriend(id, friendId);
    }

    @Override
    public void delete(User user, int friendId) {
        int id = user.getId();
        final String sql = "delete from friends where user_id = ? and friend_id = ?";
        jdbcTemplate.update(sql, id, friendId);
        updateStatusAfterDeleteFriend(id, friendId);
    }

    @Override
    public List<User> findFriendsById(int id) {
        String sql = "select * from users u inner join friends f ON f.user_id = ? and u.id = f.friend_id";
        return jdbcTemplate.query(sql, this::mapRowToUser, id);
    };

    private User mapRowToUser(ResultSet resultSet, int rowNum) throws SQLException {
        return getUserFromResultSet(resultSet);
    }

    private void updateStatusAfterAddFriend(int id, int friendId) {
        if(isContainsIdFriendId(friendId, id)) {
            jdbcTemplate.update(
                    "update friends set friendship_status_id = ? where user_id = ? and friend_id = ?",
                    INDEX_OF_AGREE_FRIENDSHIP_STATUS, id, friendId);
            jdbcTemplate.update(
                    "update friends set friendship_status_id = ? where user_id = ? and friend_id = ?",
                    INDEX_OF_AGREE_FRIENDSHIP_STATUS, friendId, id);
        }
    }

    private void updateStatusAfterDeleteFriend(int id, int friendId) {
        if(isContainsIdFriendId(friendId, id)) {
            jdbcTemplate.update(
                    "update friends set friendship_status_id = ? where user_id = ? and friend_id = ?",
                    INDEX_OF_WAITING_FRIENDSHIP_STATUS, friendId, id);
        }
    }
}
