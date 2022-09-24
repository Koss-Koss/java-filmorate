package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
            String message = "login не может содержать пробелы";
            log.warn(message);
            throw new ValidationException(message);
        }
    }

    private void validateId(User user) {
        Integer id = user.getId();
        if (id == null) {
            String message = "id пользователя - обязательное поле";
            log.warn(message);
            throw new ValidationException(message);
        }
        if (id <= 0) {
            String message = "id пользователя не может быть отрицательным или равным нулю";
            log.warn(message);
            throw new ValidationException(message);
        }
        if (!users.containsKey(id)) {
            String message = "При PUT-запросе должен существовать пользователь с указанным id";
            log.warn(message);
            throw new ValidationException(message);
        }
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<String> handleException(ValidationException exception) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(exception.getMessage());
    }

}
