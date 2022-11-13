package ru.yandex.practicum.filmorate.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class GenreDaoImpl implements GenreDao {
    private final Logger log = LoggerFactory.getLogger(GenreDaoImpl.class);
    private final JdbcTemplate jdbcTemplate;

    public GenreDaoImpl(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Genre> findById(int id) {
        SqlRowSet genreRows = jdbcTemplate.queryForRowSet("select * from genres where id = ?", id);
        if (genreRows.next()) {
            Genre genre = new Genre(
                    genreRows.getInt("id"),
                    genreRows.getString("name"));
            log.info("Найден жанр: {} {}", genre.getId(), genre.getName());
            return Optional.of(genre);
        } else {
            log.info("Жанр с идентификатором {} не найден.", id);
            return Optional.empty();
        }
    }

    @Override
    public List<Genre> findAll() {
        List<Genre> genres = new ArrayList<>();
        SqlRowSet genreRows = jdbcTemplate.queryForRowSet("select * from genres");
        while (genreRows.next()) {
            Genre genre = new Genre(
                    genreRows.getInt("id"),
                    genreRows.getString("name"));
            genres.add(genre);
        }
        log.info("Получен список всех жанров");
        return genres;
    }
}
