package ru.yandex.practicum.filmorate.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.FriendshipStatusDao;
import ru.yandex.practicum.filmorate.model.FriendshipStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class FriendshipStatusDaoImpl implements FriendshipStatusDao {
    private final Logger log = LoggerFactory.getLogger(FriendshipStatusDaoImpl.class);
    private final JdbcTemplate jdbcTemplate;

    public FriendshipStatusDaoImpl(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<FriendshipStatus> findById(int id) {
        SqlRowSet friendshipStatusRows =
                jdbcTemplate.queryForRowSet("select * from friendship_statuses where id = ?", id);

        if (friendshipStatusRows.next()) {
            FriendshipStatus friendshipStatus = new FriendshipStatus(
                    friendshipStatusRows.getInt("id"),
                    friendshipStatusRows.getString("name"));

            log.info("Найден статус дружбы: {} {}", friendshipStatus.getId(), friendshipStatus.getName());

            return Optional.of(friendshipStatus);
        } else {
            log.info("Статус дружбы с идентификатором {} не найден.", id);
            return Optional.empty();
        }
    }

    @Override
    public List<FriendshipStatus> findAll() {
        List<FriendshipStatus> friendshipStatuses = new ArrayList<>();
        SqlRowSet friendshipStatusRows = jdbcTemplate.queryForRowSet("select * from friendship_statuses");
        while (friendshipStatusRows.next()) {
            FriendshipStatus friendshipStatus = new FriendshipStatus(
                    friendshipStatusRows.getInt("id"),
                    friendshipStatusRows.getString("name"));

            friendshipStatuses.add(friendshipStatus);
        }
        log.info("Получен список всех статусов дружбы");
        return friendshipStatuses;
    }
}
