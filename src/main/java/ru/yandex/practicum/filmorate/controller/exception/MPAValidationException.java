package ru.yandex.practicum.filmorate.controller.exception;

public class MPAValidationException extends RuntimeException {
    private final String parameter;

    public MPAValidationException(String parameter) {
        this.parameter = parameter;
    }

    public String getParameter() {
        return parameter;
    }
}