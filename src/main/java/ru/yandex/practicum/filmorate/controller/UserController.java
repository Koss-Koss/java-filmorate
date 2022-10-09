package ru.yandex.practicum.filmorate.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping()
    public List<User> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public User findUser(@PathVariable("id") Integer userId) {
        return service.findUser(userId);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping()
    public User create(@Valid @RequestBody User user) {
        return service.create(user);
    }

    @PutMapping()
    public User update(@Valid @RequestBody User user) {
        return service.update(user);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable("id") Integer userId, @PathVariable("friendId") Integer friendId) {
        service.addFriend(userId, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable("id") Integer userId, @PathVariable("friendId") Integer friendId) {
        service.deleteFriend(userId, friendId);
    }

    @GetMapping("/{id}/friends")
    public Set<User> findFriendsById(@PathVariable("id") Integer userId) {
        return service.findFriendsById(userId);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Set<User> findMutualFriends(@PathVariable("id") Integer userId, @PathVariable("otherId") Integer otherUserId) {
        return service.findMutualFriends(userId, otherUserId);
    }
}
