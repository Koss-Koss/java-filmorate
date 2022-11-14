package ru.yandex.practicum.filmorate.service.validation.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.controller.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.validation.ContainsUserIdValidationService;
import ru.yandex.practicum.filmorate.storage.Storage;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Qualifier("ContainsUserIdInMemoryValidationService")
public class ContainsUserIdInMemoryValidationService implements ContainsUserIdValidationService {
    private final Storage<User> userStorage;

    public ContainsUserIdInMemoryValidationService(@Qualifier("UserStorage") Storage<User> userStorage) {
        this.userStorage = userStorage;
    }

    public void validateUserId(int id) {
        List<Integer> listId = userStorage.getAllList().stream().map(User::getId).collect(Collectors.toList());
        if (!listId.contains(id)) {
            throw new UserNotFoundException("При запросе по id должен существовать пользователь с указанным id");
        }
    }
}
