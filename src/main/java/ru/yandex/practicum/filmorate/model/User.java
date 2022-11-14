package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@Builder
public class User {
    private Integer id;
    @NotBlank(message = "email пользователя - обязательное поле и не может быть пустым")
    @Email(message = "указан некорректный email пользователя")
    private String email;
    @NotBlank(message = "login пользователя - обязательное поле и не может быть пустым")
    private String login;
    private String name;
    @PastOrPresent(message = "Дата рождения пользователя не может быть указана в будущем")
    private LocalDate birthday;
}
