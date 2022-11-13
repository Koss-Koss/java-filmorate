package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.MPADao;
import ru.yandex.practicum.filmorate.model.MPA;

import java.util.List;
import java.util.Optional;

@Service
public class MPAService {
    private final MPADao mpaDao;
    private final MPAValidationService mpaValidationService;

    public MPAService(MPADao mpaDao, MPAValidationService mpaValidationService) {
        this.mpaDao = mpaDao;
        this.mpaValidationService = mpaValidationService;
    }

    public Optional<MPA> findById(int id) {
        mpaValidationService.validateById(id);
        return mpaDao.findById(id);
    }

    public List<MPA> findAll() {
        return mpaDao.findAll();
    }
}