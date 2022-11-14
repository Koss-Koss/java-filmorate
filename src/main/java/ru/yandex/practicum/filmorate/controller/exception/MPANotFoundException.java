package ru.yandex.practicum.filmorate.controller.exception;

public class MPANotFoundException extends RuntimeException {
    private final String parameter;

    public MPANotFoundException(String parameter) {
        this.parameter = parameter;
    }

    public String getParameter() {
        return parameter;
    }
}
