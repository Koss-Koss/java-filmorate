package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.controller.exception.*;
import ru.yandex.practicum.filmorate.model.ErrorResponse;

@RestControllerAdvice
@Slf4j
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleUserValidationException(final UserValidationException e) {
        String message = String.format("Ошибка с полем %s", e.getParameter());
        log.info(message);
        return new ErrorResponse(message);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleFilmValidationException(final FilmValidationException e) {
        String message = String.format("Ошибка с полем %s", e.getParameter());
        log.info(message);
        return new ErrorResponse(message);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleUserNotFoundException(final UserNotFoundException e) {
        String message = e.getParameter();
        log.info(message);
        return new ErrorResponse(message);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleFilmNotFoundException(final FilmNotFoundException e) {
        String message = e.getParameter();
        log.info(message);
        return new ErrorResponse(message);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleGenreNotFoundException(final GenreNotFoundException e) {
        String message = e.getParameter();
        log.info(message);
        return new ErrorResponse(message);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleMPANotFoundException(final MPANotFoundException e) {
        String message = e.getParameter();
        log.info(message);
        return new ErrorResponse(message);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMPAValidationException(final MPAValidationException e) {
        String message = e.getParameter();
        log.info(message);
        return new ErrorResponse(message);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleFriendValidationException(final FriendValidationException e) {
        String message = e.getParameter();
        log.info(message);
        return new ErrorResponse(message);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleLikeValidationException(final LikeValidationException e) {
        String message = e.getParameter();
        log.info(message);
        return new ErrorResponse(message);
    }
}
