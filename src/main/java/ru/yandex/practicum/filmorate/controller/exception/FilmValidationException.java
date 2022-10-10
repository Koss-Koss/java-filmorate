package ru.yandex.practicum.filmorate.controller.exception;

public class FilmValidationException extends RuntimeException {
    private final String parameter;

    public FilmValidationException(String parameter) {
        this.parameter = parameter;
    }

    public String getParameter() {
        return parameter;
    }
}