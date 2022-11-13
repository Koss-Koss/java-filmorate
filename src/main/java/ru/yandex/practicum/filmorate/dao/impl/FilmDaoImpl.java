package ru.yandex.practicum.filmorate.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.controller.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.dao.FilmGenresDao;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.dao.MPADao;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.MPAService;

import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

@Component
public class FilmDaoImpl implements FilmDao {
    private final Logger log = LoggerFactory.getLogger(UserDaoImpl.class);
    private final JdbcTemplate jdbcTemplate;
    private final MPAService mpaService;
    private final FilmGenresDao filmGenresDao;
    private final MPADao mpaDao;
    private final GenreDao genreDao;

    public FilmDaoImpl(JdbcTemplate jdbcTemplate,
                       MPAService mpaService,
                       FilmGenresDao filmGenresDao,
                       MPADao mpaDao,
                       GenreDao genreDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.mpaService = mpaService;
        this.filmGenresDao = filmGenresDao;
        this.mpaDao = mpaDao;
        this.genreDao = genreDao;
    }

    @Override
    public Film findById(int id) {
        final String sql = "select * from films where id = ?";
        Film film;
        try {
            film = jdbcTemplate.queryForObject(sql, this::mapRowToFilm, id);
        } catch (EmptyResultDataAccessException e) {
            log.info("Фильм с идентификатором {} не найден.", id);
            throw new FilmNotFoundException(String.format("Фильм с идентификатором %d не найден", id));
        }
        return film;
    }

    @Override
    public List<Film> findAll() {
        String sql = "select * from films";
        return jdbcTemplate.query(sql, this::mapRowToFilm);
    }

    @Override
    public boolean isContainsId(int id) {
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("select * from films where id = ?", id);
        if (filmRows.next()) {
            log.info("Найден фильм с идентификатором {}", id);
            return true;
        }
        log.info("Фильм с идентификатором {} не найден.", id);
        return false;
    }

    @Override
    public Film save(Film film) {
        addingGenreAndMPANames(film);
        String sql =
            "insert into films(name, description, release_date, duration, rate, mpa_id) values (?, ?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        LocalDate releaseDate = film.getReleaseDate() == null ? null : film.getReleaseDate();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, film.getName());
            ps.setString(2, film.getDescription());
            ps.setObject(3, releaseDate);
            ps.setInt(4, film.getDuration());
            ps.setInt(5, film.getNumberOfLikes());
            ps.setInt(6, film.getMpa().isEmpty() ? null : film.getMpa().get().getId());
            return ps;
        }, keyHolder);
        int id = Objects.requireNonNull(keyHolder.getKey()).intValue();
        film.setId(id);
        if (!film.getGenres().isEmpty()) { filmGenresDao.addById(id, film.getGenres()); }
        log.info("Добавлен фильм с идентификатором {}", id);
        return findById(id);
    }

    @Override
    public Film update(int id, Film film) {
        addingGenreAndMPANames(film);
        String sql =
        "update films set name = ?, description = ?, release_date = ?, duration = ?, mpa_id = ?, rate = ? where id = ?";
        jdbcTemplate.update(sql
                , film.getName()
                , film.getDescription()
                , film.getReleaseDate()
                , film.getDuration()
                , film.getMpa().get().getId()
                , film.getNumberOfLikes()
                , id);
        filmGenresDao.deleteById(id);
        if (!film.getGenres().isEmpty()) { filmGenresDao.addById(id, film.getGenres()); }
        log.info("Изменён фильм с идентификатором {}", id);
        return findById(id);
    };

    @Override
    public List<Film> findPopularFilms(int numberOfFilms) {
        String sql = "select * from films group by id order by rate desc limit ?";
        log.info("Получен список из {} самых популярных фильмов", numberOfFilms);
        return jdbcTemplate.query(sql, this::mapRowToFilm, numberOfFilms);
    };

    private Film mapRowToFilm(ResultSet resultSet, int rowNum) throws SQLException {
        Date queryReleaseDate = resultSet.getDate("release_date");
        LocalDate releaseDate = queryReleaseDate == null ? null : queryReleaseDate.toLocalDate();
        return Film.builder()
                .id(resultSet.getInt("id"))
                .name(resultSet.getString("name"))
                .description(resultSet.getString("description"))
                .releaseDate(releaseDate)
                .duration(resultSet.getInt("duration"))
                .numberOfLikes(resultSet.getInt("rate"))
                .mpa(mpaService.findById(resultSet.getInt("mpa_id")))
                .genres(filmGenresDao.findById(resultSet.getInt("id")))
                .build();
    }
    private void addingGenreAndMPANames(Film film) {
        if (film.getMpa().isPresent()) {
            film.setMpa(mpaDao.findById(film.getMpa().get().getId()));
        }
        List<Genre> rightGenres = new ArrayList<>();
        for (Genre genre: film.getGenres()) {
            rightGenres.add(genreDao.findById(genre.getId()).get());
        }
        film.setGenres(rightGenres);
    }
}
