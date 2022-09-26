package ru.yandex.practicum.filmorate.controller.exception;

public class ValidationException extends RuntimeException {
    public ValidationException(final String message) { super(message); }
}
