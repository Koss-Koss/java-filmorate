package ru.yandex.practicum.filmorate.controller.exception;

public class FriendValidationException extends RuntimeException {
    private final String parameter;

    public FriendValidationException(String parameter) {
        this.parameter = parameter;
    }

    public String getParameter() {
        return parameter;
    }
}
