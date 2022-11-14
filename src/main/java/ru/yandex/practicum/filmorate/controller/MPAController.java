package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.service.MPAService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/mpa")
public class MPAController {
    private final MPAService mpaService;

    public MPAController(MPAService mpaService) {
        this.mpaService = mpaService;
    }

    @GetMapping()
    public List<MPA> findAll() {
        return mpaService.findAll();
    }

    @GetMapping("/{id}")
    public Optional<MPA> getGenre(@PathVariable int id){
        return mpaService.findById(id);
    }
}
