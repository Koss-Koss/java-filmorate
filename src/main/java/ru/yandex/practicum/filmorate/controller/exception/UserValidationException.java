package ru.yandex.practicum.filmorate.controller.exception;

public class UserValidationException extends RuntimeException {
    private final String parameter;

    public UserValidationException(String parameter) {
        this.parameter = parameter;
    }

    public String getParameter() {
        return parameter;
    }
}
