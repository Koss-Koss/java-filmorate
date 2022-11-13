package ru.yandex.practicum.filmorate.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.dao.LikesDao;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static ru.yandex.practicum.filmorate.dao.impl.UserDaoImpl.getUserFromResultSet;

@Component
public class LikesDaoImpl implements LikesDao {
    private final Logger log = LoggerFactory.getLogger(UserDaoImpl.class);
    private final JdbcTemplate jdbcTemplate;
    private final FilmDao filmDao;

    public LikesDaoImpl(JdbcTemplate jdbcTemplate, FilmDao filmDao){

        this.jdbcTemplate = jdbcTemplate;
        this.filmDao = filmDao;
    }

    @Override
    public boolean isContainsIdUserId(int filmId, int userId) {
        SqlRowSet userRows =
                jdbcTemplate.queryForRowSet("select * from likes where film_id = ? and user_id = ?", filmId, userId);
        if (userRows.next()) {
            log.info("У фильма id = {} найден лайк пользователя id = {}", filmId, userId);
            return true;
        }
        log.info("Лайк пользователя id = {} не найден в лайках фильма id = {} ", userId, filmId);
        return false;
    };

    @Override
    public void save(Film film, int userId) {
        int filmId = film.getId();
        jdbcTemplate.update("insert into likes (film_id, user_id) values (?, ?)", filmId, userId);
        film.setNumberOfLikes(findLikesById(filmId).size());
    };

    @Override
    public void delete(Film film, int userId) {
        int filmId = film.getId();
        jdbcTemplate.update("delete from likes where film_id = ? and user_id = ?", filmId, userId);
        film.setNumberOfLikes(findLikesById(filmId).size());
    };

    @Override
    public List<User> findLikesById(int filmId) {
        String sql = "select * from users u inner join likes l ON l.film_id = ? and u.id = l.user_id";
        return jdbcTemplate.query(sql, this::mapRowToUser, filmId);
    };

    private User mapRowToUser(ResultSet resultSet, int rowNum) throws SQLException {
        return getUserFromResultSet(resultSet);
    }
}
