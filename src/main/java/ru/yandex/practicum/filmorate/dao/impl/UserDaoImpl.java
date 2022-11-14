package ru.yandex.practicum.filmorate.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.UserDao;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

@Component
public class UserDaoImpl implements UserDao {
    private final Logger log = LoggerFactory.getLogger(UserDaoImpl.class);
    private final JdbcTemplate jdbcTemplate;

    public UserDaoImpl(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<User> findById(int id) {
        String sql = "select * from users where id = ?";
        User user;
        try {
            user = jdbcTemplate.queryForObject(sql, this::mapRowToUser, id);
        } catch (EmptyResultDataAccessException e) {
            log.info("Пользователь с идентификатором {} не найден.", id);
            return Optional.empty();
        }
        log.info("Найден пользователь с идентификатором {}", id);
        return Optional.of(user);
    }

    @Override
    public List<User> findAll() {
        String sql = "select * from users";
        return jdbcTemplate.query(sql, this::mapRowToUser);
    }

    @Override
    public boolean isContainsId(int id) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("select * from users where id = ?", id);
        if (userRows.next()) {
            log.info("Найден пользователь с идентификатором {}", id);
            return true;
        }
        log.info("Пользователь с идентификатором {} не найден.", id);
        return false;
    }

    @Override
    public User save(User user) {
        String sql = "insert into users(email, login, name, birthday) values (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        LocalDate birthday = user.getBirthday() == null ? null : user.getBirthday();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getLogin());
            ps.setString(3, user.getName());
            ps.setObject(4, birthday);
            return ps;
        }, keyHolder);
        int id = Objects.requireNonNull(keyHolder.getKey()).intValue();
        user.setId(id);
        return findById(id).get();
    };

    @Override
    public User update(int id, User user) {
        String sql = "update users set email = ?, login = ?, name = ?, birthday = ? where id = ?";
        jdbcTemplate.update(sql
                , user.getEmail()
                , user.getLogin()
                , user.getName()
                , user.getBirthday()
                , id);
        return findById(id).get();
    }

    private User mapRowToUser(ResultSet resultSet, int rowNum) throws SQLException {
        return getUserFromResultSet(resultSet);
    }

    protected static User getUserFromResultSet(ResultSet resultSet) throws SQLException {
        Date queryBirthday = resultSet.getDate("birthday");
        LocalDate birthday = queryBirthday == null ? null : queryBirthday.toLocalDate();
        return User.builder()
                .id(resultSet.getInt("id"))
                .email(resultSet.getString("email"))
                .login(resultSet.getString("login"))
                .name(resultSet.getString("name"))
                .birthday(birthday)
                .build();
    }
}