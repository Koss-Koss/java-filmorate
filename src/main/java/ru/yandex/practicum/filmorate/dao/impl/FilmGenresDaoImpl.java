package ru.yandex.practicum.filmorate.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.FilmGenresDao;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.ArrayList;
import java.util.List;

@Component
public class FilmGenresDaoImpl implements FilmGenresDao {
    private final Logger log = LoggerFactory.getLogger(FilmGenresDaoImpl.class);
    private final JdbcTemplate jdbcTemplate;
    private final GenreDao genreDao;

    public FilmGenresDaoImpl(JdbcTemplate jdbcTemplate, GenreDao genreDao){
        this.jdbcTemplate = jdbcTemplate;
        this.genreDao = genreDao;
    }

    @Override
    public List<Genre> findById(int id) {
        List<Genre> genres = new ArrayList<>();
        SqlRowSet mpaRows = jdbcTemplate.queryForRowSet("select genre_id from film_genres where film_id = ?", id);
        boolean entered = false;
        while (mpaRows.next()) {
            entered = true;
            int genreId = mpaRows.getInt("genre_id");
            genres.add(genreDao.findById(genreId).get());
            log.info("Найден жанр: id = {} для фильма id = {}", genreId, id);
        } if(!entered) {
            log.info("Жанры для фильма с идентификатором {} не найдены.", id);
        }
        return genres;
    };

    @Override
    public void addById(int id, List<Genre> genres) {
        for (Genre genre: genres) {
            jdbcTemplate.update("insert into film_genres (film_id, genre_id) values (?, ?)", id, genre.getId());
        }
    };

    @Override
    public void deleteById(int id){
        jdbcTemplate.update("delete from film_genres where film_id = ?", id);
    };
}
