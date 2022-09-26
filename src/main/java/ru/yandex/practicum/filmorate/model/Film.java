package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class Film {
    private Integer id;
    @NotBlank(message = "Название фильма - обязательное поле и не может быть пустым")
    private String name;
    @Size(max = 200, message = "Длина описания - не более 200 символов")
    private String description;
    private LocalDate releaseDate;
    @Positive(message = "Продолжительность фильма указыввается в минутах и должна быть положительной")
    private int duration;

}
