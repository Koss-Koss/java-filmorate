package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.*;

@Data
@AllArgsConstructor
@Builder
public class Film implements Comparable<Film> {
    private Integer id;
    @NotBlank(message = "Название фильма - обязательное поле и не может быть пустым")
    private String name;
    @Size(max = 200, message = "Длина описания - не более 200 символов")
    private String description;
    private LocalDate releaseDate;
    @Positive(message = "Продолжительность фильма указыввается в минутах и должна быть положительной")
    private int duration;
    private int numberOfLikes;
    private Optional<MPA> mpa;
    List<Genre> genres;

    @Override
    public int compareTo(Film o) {
        return Comparator.comparing(Film::getNumberOfLikes).reversed()
                .thenComparingInt(Film::getId)
                .compare(this, o);
    }
}
