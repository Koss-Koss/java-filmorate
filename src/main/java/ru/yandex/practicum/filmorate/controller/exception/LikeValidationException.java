package ru.yandex.practicum.filmorate.controller.exception;

public class LikeValidationException extends RuntimeException {
    private final String parameter;

    public LikeValidationException(String parameter) { this.parameter = parameter; }

    public String getParameter() { return parameter; }
}