package ru.yandex.practicum.filmorate.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.MPADao;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.MPA;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class MPADaoImpl implements MPADao {
    private final Logger log = LoggerFactory.getLogger(MPADaoImpl.class);
    private final JdbcTemplate jdbcTemplate;

    public MPADaoImpl(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<MPA> findById(int id) {
        SqlRowSet mpaRows = jdbcTemplate.queryForRowSet("select * from MPA where id = ?", id);
        if (mpaRows.next()) {
            MPA mpa = new MPA(
                    mpaRows.getInt("id"),
                    mpaRows.getString("name"));
            log.info("Найден жанр: {} {}", mpa.getId(), mpa.getName());
            return Optional.of(mpa);
        } else {
            log.info("Рейтинг MPA с идентификатором {} не найден.", id);
            return Optional.empty();
        }
    }

    @Override
    public List<MPA> findAll() {
        List<MPA> mpas = new ArrayList<>();
        SqlRowSet mpaRows = jdbcTemplate.queryForRowSet("select * from MPA");
        while (mpaRows.next()) {
            MPA mpa = new MPA(
                    mpaRows.getInt("id"),
                    mpaRows.getString("name"));
            mpas.add(mpa);
        }
        log.info("Получен список всех жанров");
        return mpas;
    }

    @Override
    public boolean isContainsId(int id) {
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("select * from MPA where id = ?", id);
        if (filmRows.next()) {
            log.info("Найден рейтинг MPA с идентификатором {}", id);
            return true;
        }
        log.info("Рейтинг MPA с идентификатором {} не найден.", id);
        return false;
    };
}
