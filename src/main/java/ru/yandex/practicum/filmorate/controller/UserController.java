package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.controller.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();
    private int currentUserId = 1;

    @GetMapping()
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping()
    public User create(@Valid @RequestBody User user) {
        validateLogin(user);
        String name = user.getName();
        if (name == null || name.equals("")) { user.setName(user.getLogin()); }
        user.setId(currentUserId);
        users.put(currentUserId, user);
        log.info("Добавлен пользователь: {}", user);
        return users.get(currentUserId++);
    }

    @PutMapping()
    public User update(@Valid @RequestBody User user) {
        validateId(user);
        validateLogin(user);
        String name = user.getName();
        if (name == null || name.equals("")) { user.setName(user.getLogin()); }
        users.put(user.getId(), user);
        log.info("Отредактирован пользователь: {}", user);
        return users.get(user.getId());
    }

    private void validateLogin(User user) {
        if (user.getLogin().contains(" ")) {
            throw new ValidationException("login не может содержать пробелы");
        }
    }

    private void validateId(User user) {
        Integer id = user.getId();
        if (id == null) {
            throw new ValidationException("id пользователя - обязательное поле");
        }
        if (id <= 0) {
            throw new ValidationException("id пользователя не может быть отрицательным или равным нулю");
        }
        if (!users.containsKey(id)) {
            throw new ValidationException("При PUT-запросе должен существовать пользователь с указанным id");
        }
    }

}
