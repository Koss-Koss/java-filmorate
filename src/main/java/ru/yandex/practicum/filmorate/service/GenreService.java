package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Optional;

@Service
public class GenreService {
    private final GenreDao genreDao;
    private final GenreValidationService genreValidationService;

    public GenreService(GenreDao genreDao, GenreValidationService genreValidationService) {
        this.genreDao = genreDao;
        this.genreValidationService = genreValidationService;
    }

    public Optional<Genre> findById(int id) {
        Optional<Genre> genre = genreDao.findById(id);
        genreValidationService.validateById(genre);
        return genre;
    }

    public List<Genre> findAll() {
        return genreDao.findAll();
    }
}
