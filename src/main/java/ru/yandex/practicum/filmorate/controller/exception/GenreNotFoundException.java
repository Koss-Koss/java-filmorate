package ru.yandex.practicum.filmorate.controller.exception;

public class GenreNotFoundException extends RuntimeException {
    private final String parameter;

    public GenreNotFoundException(String parameter) {
        this.parameter = parameter;
    }

    public String getParameter() {
        return parameter;
    }
}
