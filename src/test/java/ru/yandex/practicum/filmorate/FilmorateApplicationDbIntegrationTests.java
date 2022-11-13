package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.controller.exception.*;
import ru.yandex.practicum.filmorate.dao.*;
import ru.yandex.practicum.filmorate.model.*;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.yandex.practicum.filmorate.service.FilmValidationService.BEGINNING_OF_CINEMA_ERA;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmorateApplicationDbIntegrationTests {

    private final JdbcTemplate jdbcTemplate;
    private final MPADao mpaDao;
    private final GenreDao genreDao;
    private final FriendshipStatusDao friendshipStatusDao;
    private final UserDao userDao;
    private final FriendsDao friendsDao;
    private final FilmDao filmDao;
    private final LikesDao likesDao;

    private final User testUser = User.builder()
            .email("leon@test.com")
            .login("leo")
            .name("Leonid")
            .birthday(LocalDate.now())
            .build();

    private final User testFriend = User.builder()
            .email("ivan@test2.com")
            .login("ivan")
            .name("Ivan")
            .birthday(LocalDate.of(2000, Month.JANUARY, 01))
            .build();

    private final User testCommomFriend = User.builder()
            .email("wallace@ithaca.us")
            .login("david")
            .name("David Foster Wallace")
            .birthday(LocalDate.of(1962, Month.FEBRUARY, 21))
            .build();

    MPA mpa1 = new MPA(1, "G");
    MPA mpa5 = new MPA(5, "NC-17");
    Genre genre1 = new Genre(1, "Комедия");
    Genre genre3 = new Genre(3, "Мультфильм");
    Genre genre6 = new Genre(6, "Боевик");

    private final Film testFilmOne = Film.builder()
            .name("Запах женщины")
            .description("Test".repeat(50))
            .releaseDate(BEGINNING_OF_CINEMA_ERA)
            .duration(144)
            .mpa(Optional.of(mpa1))
            .genres(new ArrayList<Genre>())
            .build();

    private final Film testFilmSecond = Film.builder()
            .name("Телохранитель")
            .description("Test".repeat(50))
            .releaseDate(LocalDate.of(1992, Month.NOVEMBER, 25))
            .duration(143)
            .mpa(Optional.of(mpa5))
            .genres(new ArrayList<Genre>())
            .build();

    private final Film testFilmThird = Film.builder()
            .name("Девчата")
            .description("Test".repeat(50))
            .releaseDate(LocalDate.of(1962, Month.MARCH, 7))
            .duration(92)
            .mpa(Optional.of(mpa1))
            .genres(new ArrayList<Genre>())
            .build();

    //Integration tests

    @AfterEach
    void tearDown() {
        jdbcTemplate.update("delete from likes");
        jdbcTemplate.update("delete from film_genres");
        jdbcTemplate.update("delete from friends");
        jdbcTemplate.update("delete from users");
        jdbcTemplate.update("delete from films");
        jdbcTemplate.update("alter table users alter column id restart with 1");
        jdbcTemplate.update("alter table films alter column id restart with 1");
    }

    @Test
    public void existingMPATest() {
        assertThat(mpaDao.findById(1))
                .isPresent()
                .hasValueSatisfying(a ->
                        assertThat(a).hasFieldOrPropertyWithValue("id", 1).hasFieldOrPropertyWithValue("name", "G")
                );
        assertThat(mpaDao.isContainsId(1)).isTrue();
    }

    @Test
    public void notExistingMPATest() {
        assertThat(mpaDao.findById(-1)).isEmpty();
        assertThat(mpaDao.isContainsId(-1)).isFalse();
        assertThat(mpaDao.findById(999)).isEmpty();
        assertThat(mpaDao.isContainsId(999)).isFalse();
    }

    @Test
    public void findAllMPATest() {
        List<MPA> mpas = mpaDao.findAll();

        assertThat(mpas).isNotNull();
        assertThat(mpas.size()).isEqualTo(5);
        assertThat(mpas.get(0)).isEqualTo(mpa1);
        assertThat(mpas.get(4)).isEqualTo(mpa5);
    }

    @Test
    public void existingGenreTest() {
        assertThat(genreDao.findById(1))
                .isPresent()
                .hasValueSatisfying(a ->
                        assertThat(a).hasFieldOrPropertyWithValue("id", 1)
                                .hasFieldOrPropertyWithValue("name", "Комедия")
                );
    }

    @Test
    public void notExistingGenreTest() {
        assertThat(genreDao.findById(-1)).isEmpty();
        assertThat(genreDao.findById(999)).isEmpty();
    }

    @Test
    public void findAllGenresTest() {
        List<Genre> genres = genreDao.findAll();

        assertThat(genres).isNotNull();
        assertThat(genres.size()).isEqualTo(6);
        assertThat(genres.get(0)).isEqualTo(genre1);
        assertThat(genres.get(5)).isEqualTo(genre6);
    }

    @Test
    public void existingFriendshipStatusTest() {
        assertThat(friendshipStatusDao.findById(1))
                .isPresent()
                .hasValueSatisfying(a ->
                        assertThat(a).hasFieldOrPropertyWithValue("id", 1)
                                .hasFieldOrPropertyWithValue("name", "waiting")
                );
    }

    @Test
    public void notExistingFriendshipStatusTest() {
        assertThat(friendshipStatusDao.findById(-1)).isEmpty();
        assertThat(friendshipStatusDao.findById(999)).isEmpty();
    }

    @Test
    public void findAllFriendshipStatusTest() {
        List<FriendshipStatus> statuses = friendshipStatusDao.findAll();

        assertThat(statuses).isNotNull();
        assertThat(statuses.size()).isEqualTo(2);
        assertThat(statuses.get(0)).isEqualTo(new FriendshipStatus(1, "waiting"));
        assertThat(statuses.get(1)).isEqualTo(new FriendshipStatus(2, "agree"));
    }

    @Test
    public void validUserCreateTest() {
        userDao.save(testUser);

        List<User> users = userDao.findAll();
        User savedTestUser = users.get(users.size() - 1);

        assertThat(users.size()).isEqualTo(1);
        assertThat(savedTestUser).isNotNull();
        assertThat(savedTestUser.getEmail()).isEqualTo(testUser.getEmail());
        assertThat(savedTestUser.getLogin()).isEqualTo(testUser.getLogin());
        assertThat(savedTestUser.getName()).isEqualTo(testUser.getName());
        assertThat(savedTestUser.getBirthday()).isEqualTo(testUser.getBirthday());
    }

    @Test
    public void invalidLoginUserCreateTest() {
        User badUser = User.builder()
                .email("leon@test.com")
                .login("le o")
                .birthday(LocalDate.of(2022, Month.FEBRUARY, 22))
                .build();
        try {
            userDao.save(badUser);
        } catch (DataIntegrityViolationException e) {}

        assertThat(userDao.findAll().size()).isEqualTo(0);
    }

    @Test
    public void invalidEmailUserCreateTest() {
        User badUser = User.builder()
                .login("leo")
                .name("")
                .email("test.com")
                .birthday(LocalDate.of(2022, Month.FEBRUARY, 22))
                .build();
        try {
            userDao.save(badUser);
        } catch (DataIntegrityViolationException e) {
        }

        assertThat(userDao.findAll().size()).isEqualTo(0);
    }

    @Test
    public void invalidBirthdayUserCreateTest() {
        User badUser = User.builder()
                .login("leo")
                .name("Leonid")
                .email("1@test.com")
                .birthday(LocalDate.now().plusDays(1))
                .build();
        try {
            userDao.save(badUser);
        } catch (DataIntegrityViolationException e) {
        }

        assertThat(userDao.findAll().size()).isEqualTo(0);
    }

    @Test
    public void validUserUpdateTest() {
        userDao.save(testUser);
        User updateUser = User.builder()
                .id(1)
                .login("ivan")
                .name("Ivan")
                .email("2@test2.com")
                .birthday(LocalDate.of(2000, Month.JANUARY, 01))
                .build();
        userDao.update(updateUser.getId(), updateUser);

        List<User> users = userDao.findAll();
        User savedUpdateUser = users.get(users.size() - 1);

        assertThat(users.size()).isEqualTo(1);
        assertThat(savedUpdateUser).isNotNull();
        assertThat(savedUpdateUser.getEmail()).isEqualTo(updateUser.getEmail());
        assertThat(savedUpdateUser.getLogin()).isEqualTo(updateUser.getLogin());
        assertThat(savedUpdateUser.getName()).isEqualTo(updateUser.getName());
        assertThat(savedUpdateUser.getBirthday()).isEqualTo(updateUser.getBirthday());
    }

    @Test
    public void invalidUserUpdateTest() {
        userDao.save(testUser);
        User badUpdateUser = User.builder()
                .id(999)
                .login("ivan")
                .name("Ivan")
                .email("2@test2.com")
                .birthday(LocalDate.of(2000, Month.JANUARY, 01))
                .build();
        try {
            userDao.update(badUpdateUser.getId(), badUpdateUser);
        } catch (NoSuchElementException e) {
        }

        List<User> users = userDao.findAll();
        User savedUpdateUser = users.get(users.size() - 1);

        assertThat(users.size()).isEqualTo(1);
        assertThat(savedUpdateUser).isNotNull();
        assertThat(savedUpdateUser.getEmail()).isEqualTo(testUser.getEmail());
        assertThat(savedUpdateUser.getLogin()).isEqualTo(testUser.getLogin());
        assertThat(savedUpdateUser.getName()).isEqualTo(testUser.getName());
        assertThat(savedUpdateUser.getBirthday()).isEqualTo(testUser.getBirthday());
    }

    @Test
    public void findUserAllAndFriendCreateTest() {
        userDao.save(testUser);
        userDao.save(testFriend);
        assertThat(userDao.isContainsId(testUser.getId())).isTrue();
        assertThat(userDao.isContainsId(3)).isFalse();

        List<User> users = userDao.findAll();
        User savedTestUser = users.get(users.size() - 2);
        User savedTestFriend = users.get(users.size() - 1);

        assertThat(users.size()).isEqualTo(2);
        assertThat(savedTestUser).isNotNull();
        assertThat(savedTestUser.getEmail()).isEqualTo(testUser.getEmail());
        assertThat(savedTestUser.getLogin()).isEqualTo(testUser.getLogin());
        assertThat(savedTestUser.getName()).isEqualTo(testUser.getName());
        assertThat(savedTestUser.getBirthday()).isEqualTo(testUser.getBirthday());
        assertThat(savedTestFriend).isNotNull();
        assertThat(savedTestFriend.getEmail()).isEqualTo(testFriend.getEmail());
        assertThat(savedTestFriend.getLogin()).isEqualTo(testFriend.getLogin());
        assertThat(savedTestFriend.getName()).isEqualTo(testFriend.getName());
        assertThat(savedTestFriend.getBirthday()).isEqualTo(testFriend.getBirthday());
    }

    @Test
    public void findByIdCommonFriendTest() {
        userDao.save(testUser);
        userDao.save(testFriend);
        userDao.save(testCommomFriend);

        List<User> users = userDao.findAll();
        assertThat(users.size()).isEqualTo(3);
        Optional<User> savedTestCommonFriend = userDao.findById(testCommomFriend.getId());
        assertThat(savedTestCommonFriend).isNotNull();
        assertThat(savedTestCommonFriend.get().getEmail()).isEqualTo(testCommomFriend.getEmail());
        assertThat(savedTestCommonFriend.get().getLogin()).isEqualTo(testCommomFriend.getLogin());
        assertThat(savedTestCommonFriend.get().getName()).isEqualTo(testCommomFriend.getName());
        assertThat(savedTestCommonFriend.get().getBirthday()).isEqualTo(testCommomFriend.getBirthday());
    }

    @Test
    public void notExistingUserTest() {
        userDao.save(testUser);

        assertThat(userDao.findById(-1)).isEmpty();
        assertThat(userDao.findById(2)).isEmpty();
    }

    @Test
    public void userAddFriendCreateTest() {
        userDao.save(testUser);
        userDao.save(testFriend);
        userDao.save(testCommomFriend);
        friendsDao.save(testUser, testFriend.getId());
        List<User> friends = friendsDao.findFriendsById(testUser.getId());
        User savedTestFriend = friends.get(friends.size() - 1);

        assertThat(friendsDao.isContainsIdFriendId(testUser.getId(), testFriend.getId())).isTrue();
        assertThat(friendsDao.isContainsIdFriendId(testFriend.getId(), testUser.getId())).isFalse();
        assertThat(friends.size()).isEqualTo(1);
        assertThat(savedTestFriend).isNotNull();
        assertThat(savedTestFriend.getEmail()).isEqualTo(testFriend.getEmail());
        assertThat(savedTestFriend.getLogin()).isEqualTo(testFriend.getLogin());
        assertThat(savedTestFriend.getName()).isEqualTo(testFriend.getName());
        assertThat(savedTestFriend.getBirthday()).isEqualTo(testFriend.getBirthday());
    }

    @Test
    public void userAddInvalidFriendCreateTest() {
        userDao.save(testUser);
        try {
            friendsDao.save(testUser, -1);
        } catch (DataIntegrityViolationException e) {
        }
        List<User> friends = friendsDao.findFriendsById(testUser.getId());

        assertThat(friends.size()).isEqualTo(0);
    }

    @Test
    public void findUsersFriendsEmptyTest() {
        userDao.save(testUser);
        userDao.save(testFriend);
        List<User> friends = friendsDao.findFriendsById(testFriend.getId());

        assertThat(friends.size()).isEqualTo(0);
    }

    @Test
    public void userAddSecondFriendCreateTest() {
        userDao.save(testUser);
        userDao.save(testFriend);
        userDao.save(testCommomFriend);
        friendsDao.save(testUser, testFriend.getId());
        friendsDao.save(testUser, testCommomFriend.getId());
        List<User> friends = friendsDao.findFriendsById(testUser.getId());

        assertThat(friendsDao.isContainsIdFriendId(testUser.getId(), testFriend.getId())).isTrue();
        assertThat(friendsDao.isContainsIdFriendId(testUser.getId(), testCommomFriend.getId())).isTrue();
        assertThat(friends.size()).isEqualTo(2);
    }

    @Test
    public void usersRemoveFriendDeleteTest() {
        userDao.save(testUser);
        userDao.save(testFriend);
        userDao.save(testCommomFriend);
        friendsDao.save(testUser, testFriend.getId());
        friendsDao.save(testUser, testCommomFriend.getId());
        friendsDao.delete(testUser, testFriend.getId());
        List<User> friends = friendsDao.findFriendsById(testUser.getId());

        assertThat(friendsDao.isContainsIdFriendId(testUser.getId(), testFriend.getId())).isFalse();
        assertThat(friendsDao.isContainsIdFriendId(testUser.getId(), testCommomFriend.getId())).isTrue();
        assertThat(friends.size()).isEqualTo(1);
    }

    @Test
    public void findFilmAllEmptyTest() { assertThat(filmDao.findAll().size()).isEqualTo(0);	}

    @Test
    public void validFilmCreateTest() {
        filmDao.save(testFilmOne);

        List<Film> films = filmDao.findAll();
        Film savedTestFilmOne = films.get(films.size() - 1);

        assertThat(films.size()).isEqualTo(1);
        assertThat(savedTestFilmOne).isNotNull();
        assertThat(savedTestFilmOne.getId()).isEqualTo(1);
        assertThat(savedTestFilmOne.getName()).isEqualTo(testFilmOne.getName());
        assertThat(savedTestFilmOne.getDescription()).isEqualTo(testFilmOne.getDescription());
        assertThat(savedTestFilmOne.getReleaseDate()).isEqualTo(testFilmOne.getReleaseDate());
        assertThat(savedTestFilmOne.getDuration()).isEqualTo(testFilmOne.getDuration());
        assertThat(savedTestFilmOne.getMpa()).isEqualTo(testFilmOne.getMpa());
    }

    @Test
    public void invalidNameFilmCreateTest() {
        final Film badFilm = Film.builder()
                .name("")
                .description("test description")
                .releaseDate(LocalDate.of(1992, Month.NOVEMBER, 25))
                .duration(143)
                .mpa(Optional.of(mpa5))
                .genres(new ArrayList<Genre>())
                .build();
        try {
            filmDao.save(badFilm);
        } catch (DataIntegrityViolationException e) {}

        assertThat(filmDao.findAll().size()).isEqualTo(0);
    }

    @Test
    public void invalidDescriptionFilmCreateTest() {
        final Film badFilm = Film.builder()
                .name("Запах женщины")
                .description("Test".repeat(50) + " ")
                .releaseDate(LocalDate.of(1992, Month.NOVEMBER, 25))
                .duration(143)
                .mpa(Optional.of(mpa5))
                .genres(new ArrayList<Genre>())
                .build();
        try {
            filmDao.save(badFilm);
        } catch (DataIntegrityViolationException e) {}

        assertThat(filmDao.findAll().size()).isEqualTo(0);
    }

    @Test
    public void invalidReleaseDateFilmCreateTest() {
        final Film badFilm = Film.builder()
                .name("Запах женщины")
                .description("Test".repeat(50))
                .releaseDate(BEGINNING_OF_CINEMA_ERA.minusDays(1))
                .duration(143)
                .mpa(Optional.of(mpa5))
                .genres(new ArrayList<Genre>())
                .build();
        try {
            filmDao.save(badFilm);
        } catch (DataIntegrityViolationException e) {}

        assertThat(filmDao.findAll().size()).isEqualTo(0);
    }

    @Test
    public void invalidDurationFilmCreateTest() {
        final Film badFilm = Film.builder()
                .name("Запах женщины")
                .description("Test".repeat(50))
                .releaseDate(LocalDate.of(1992, Month.NOVEMBER, 25))
                .duration(-1)
                .mpa(Optional.of(mpa5))
                .genres(new ArrayList<Genre>())
                .build();
        try {
            filmDao.save(badFilm);
        } catch (DataIntegrityViolationException e) {}

        assertThat(filmDao.findAll().size()).isEqualTo(0);

        final Film badFilm2 = Film.builder()
                .name("Запах женщины")
                .description("Test".repeat(50))
                .releaseDate(LocalDate.of(1992, Month.NOVEMBER, 25))
                .duration(0)
                .mpa(Optional.of(mpa5))
                .genres(new ArrayList<Genre>())
                .build();
        try {
            filmDao.save(badFilm2);
        } catch (DataIntegrityViolationException e) {}

        assertThat(filmDao.findAll().size()).isEqualTo(0);
    }

    @Test
    public void invalidMPAFilmCreateTest() {
        final Film badFilm = Film.builder()
                .name("Запах женщины")
                .description("Test".repeat(50))
                .releaseDate(LocalDate.of(1992, Month.NOVEMBER, 25))
                .duration(143)
                .mpa(null)
                .genres(new ArrayList<Genre>())
                .build();
        try {
            filmDao.save(badFilm);
        } catch (NullPointerException e) {}

        assertThat(filmDao.findAll().size()).isEqualTo(0);
    }

    @Test
    public void validFilmUpdateTest() {
        filmDao.save(testFilmOne);
        Film updateFilm = Film.builder()
                .id(1)
                .name("Офицеры")
                .description("Классика")
                .releaseDate(LocalDate.of(1971, Month.JULY, 26))
                .duration(97)
                .mpa(Optional.of(mpa5))
                .genres(new ArrayList<Genre>())
                .build();
        filmDao.update(updateFilm.getId(), updateFilm);

        List<Film> films = filmDao.findAll();
        Film savedUpdateFilm = films.get(films.size() - 1);

        assertThat(films.size()).isEqualTo(1);
        assertThat(savedUpdateFilm).isNotNull();
        assertThat(savedUpdateFilm.getName()).isEqualTo(updateFilm.getName());
        assertThat(savedUpdateFilm.getDescription()).isEqualTo(updateFilm.getDescription());
        assertThat(savedUpdateFilm.getReleaseDate()).isEqualTo(updateFilm.getReleaseDate());
        assertThat(savedUpdateFilm.getDuration()).isEqualTo(updateFilm.getDuration());
        assertThat(savedUpdateFilm.getMpa()).isEqualTo(updateFilm.getMpa());
    }

    @Test
    public void invalidFilmUpdateTest() {
        filmDao.save(testFilmOne);
        Film badUpdateFilm = Film.builder()
                .id(999)
                .name("Офицеры")
                .description("Классика")
                .releaseDate(LocalDate.of(1971, Month.JULY, 26))
                .duration(97)
                .mpa(Optional.of(mpa5))
                .genres(new ArrayList<Genre>())
                .build();
        try {
            filmDao.update(badUpdateFilm.getId(), badUpdateFilm);
        } catch (NoSuchElementException e) {
        } catch (FilmNotFoundException e) {
        }


        List<Film> films = filmDao.findAll();
        Film savedUpdateFilm = films.get(films.size() - 1);

        assertThat(films.size()).isEqualTo(1);
        assertThat(savedUpdateFilm).isNotNull();
        assertThat(savedUpdateFilm.getName()).isEqualTo(testFilmOne.getName());
        assertThat(savedUpdateFilm.getDescription()).isEqualTo(testFilmOne.getDescription());
        assertThat(savedUpdateFilm.getReleaseDate()).isEqualTo(testFilmOne.getReleaseDate());
        assertThat(savedUpdateFilm.getDuration()).isEqualTo(testFilmOne.getDuration());
        assertThat(savedUpdateFilm.getMpa()).isEqualTo(testFilmOne.getMpa());
    }

    @Test
    public void findPopularFilmWithoutLikesTest() {
        filmDao.save(testFilmOne);
        List<Film> films = filmDao.findPopularFilms(10);
        Film bestPopularFilm = films.get(films.size() - 1);

        assertThat(films.size()).isEqualTo(1);
        assertThat(bestPopularFilm).isNotNull();
        assertThat(bestPopularFilm.getName()).isEqualTo(testFilmOne.getName());
        assertThat(bestPopularFilm.getDescription()).isEqualTo(testFilmOne.getDescription());
        assertThat(bestPopularFilm.getReleaseDate()).isEqualTo(testFilmOne.getReleaseDate());
        assertThat(bestPopularFilm.getDuration()).isEqualTo(testFilmOne.getDuration());
        assertThat(bestPopularFilm.getMpa()).isEqualTo(testFilmOne.getMpa());
    }

    @Test
    public void secondFilmCreateTest() {
        filmDao.save(testFilmOne);
        filmDao.save(testFilmSecond);
        assertThat(filmDao.isContainsId(testFilmOne.getId())).isTrue();
        assertThat(filmDao.isContainsId(testFilmSecond.getId())).isTrue();
        assertThat(filmDao.isContainsId(3)).isFalse();

        List<Film> films = filmDao.findAll();
        Film savedSecondFilm = films.get(films.size() - 1);

        assertThat(films.size()).isEqualTo(2);
        assertThat(savedSecondFilm).isNotNull();
        assertThat(savedSecondFilm.getName()).isEqualTo(testFilmSecond.getName());
        assertThat(savedSecondFilm.getDescription()).isEqualTo(testFilmSecond.getDescription());
        assertThat(savedSecondFilm.getReleaseDate()).isEqualTo(testFilmSecond.getReleaseDate());
        assertThat(savedSecondFilm.getDuration()).isEqualTo(testFilmSecond.getDuration());
        assertThat(savedSecondFilm.getMpa()).isEqualTo(testFilmSecond.getMpa());
    }

    @Test
    public void findByIdOneFilmTest() {
        filmDao.save(testFilmOne);

        Film savedOneFilm = filmDao.findById(testFilmOne.getId());

        assertThat(savedOneFilm).isNotNull();
        assertThat(savedOneFilm.getName()).isEqualTo(testFilmOne.getName());
        assertThat(savedOneFilm.getDescription()).isEqualTo(testFilmOne.getDescription());
        assertThat(savedOneFilm.getReleaseDate()).isEqualTo(testFilmOne.getReleaseDate());
        assertThat(savedOneFilm.getDuration()).isEqualTo(testFilmOne.getDuration());
        assertThat(savedOneFilm.getMpa()).isEqualTo(testFilmOne.getMpa());
    }

    @Test
    public void notExistingFilmTest() {
        filmDao.save(testFilmOne);

        try {
            assertThat(filmDao.findById(-1)).isNull();
            assertThat(filmDao.findById(2)).isNull();
        } catch (FilmNotFoundException e) {
        }
    }

    @Test
    public void findByIdSecondFilmTest() {
        filmDao.save(testFilmOne);
        filmDao.save(testFilmSecond);

        Film savedSecondFilm = filmDao.findById(testFilmSecond.getId());

        assertThat(savedSecondFilm).isNotNull();
        assertThat(savedSecondFilm.getName()).isEqualTo(testFilmSecond.getName());
        assertThat(savedSecondFilm.getDescription()).isEqualTo(testFilmSecond.getDescription());
        assertThat(savedSecondFilm.getReleaseDate()).isEqualTo(testFilmSecond.getReleaseDate());
        assertThat(savedSecondFilm.getDuration()).isEqualTo(testFilmSecond.getDuration());
        assertThat(savedSecondFilm.getMpa()).isEqualTo(testFilmSecond.getMpa());
    }

    @Test
    public void filmAddLikeCreateTest() {
        userDao.save(testUser);
        filmDao.save(testFilmOne);
        filmDao.save(testFilmSecond);
        likesDao.save(testFilmSecond, testUser.getId());
        List<User> likesUsers = likesDao.findLikesById(testFilmSecond.getId());
        User savedTestUser = likesUsers.get(likesUsers.size() - 1);

        assertThat(likesDao.isContainsIdUserId(testFilmSecond.getId(), testUser.getId())).isTrue();
        assertThat(likesDao.isContainsIdUserId(testUser.getId(), testFilmSecond.getId())).isFalse();
        assertThat(likesUsers.size()).isEqualTo(1);
        assertThat(savedTestUser).isNotNull();
        assertThat(savedTestUser.getEmail()).isEqualTo(testUser.getEmail());
        assertThat(savedTestUser.getLogin()).isEqualTo(testUser.getLogin());
        assertThat(savedTestUser.getName()).isEqualTo(testUser.getName());
        assertThat(savedTestUser.getBirthday()).isEqualTo(testUser.getBirthday());
    }

    @Test
    public void filmAddInvalidLikeCreateTest() {
        filmDao.save(testFilmOne);
        try {
            likesDao.save(testFilmOne, -1);
        } catch (DataIntegrityViolationException e) {
        }
        List<User> likesUsers = likesDao.findLikesById(testFilmOne.getId());

        assertThat(likesUsers.size()).isEqualTo(0);
    }

    @Test
    public void findPopularFilmCountOneTest() {
        userDao.save(testUser);
        filmDao.save(testFilmOne);
        filmDao.save(testFilmSecond);
        likesDao.save(testFilmOne, testUser.getId());
        filmDao.save(testFilmOne);

        List<Film> films = filmDao.findPopularFilms(1);
        Film bestPopularFilm = films.get(films.size() - 1);

        assertThat(films.size()).isEqualTo(1);
        assertThat(bestPopularFilm).isNotNull();
        assertThat(bestPopularFilm.getName()).isEqualTo(testFilmOne.getName());
        assertThat(bestPopularFilm.getDescription()).isEqualTo(testFilmOne.getDescription());
        assertThat(bestPopularFilm.getReleaseDate()).isEqualTo(testFilmOne.getReleaseDate());
        assertThat(bestPopularFilm.getDuration()).isEqualTo(testFilmOne.getDuration());
        assertThat(bestPopularFilm.getMpa()).isEqualTo(testFilmOne.getMpa());
    }

    @Test
    public void filmRemoveLikeDeleteTest() {
        userDao.save(testUser);
        userDao.save(testFriend);
        filmDao.save(testFilmOne);
        likesDao.save(testFilmOne, testUser.getId());
        likesDao.save(testFilmOne, testFriend.getId());
        likesDao.delete(testFilmOne, testUser.getId());
        List<User> likesUsers = likesDao.findLikesById(testFilmOne.getId());

        assertThat(likesDao.isContainsIdUserId(testFilmOne.getId(), testUser.getId())).isFalse();
        assertThat(likesDao.isContainsIdUserId(testFilmOne.getId(), testFriend.getId())).isTrue();
        assertThat(likesUsers.size()).isEqualTo(1);
    }

    @Test
    public void findPopularFilmCountTwoTest() {
        userDao.save(testUser);
        userDao.save(testFriend);
        filmDao.save(testFilmOne);
        filmDao.save(testFilmSecond);
        filmDao.save(testFilmThird);
        likesDao.save(testFilmSecond, testUser.getId());
        likesDao.save(testFilmSecond, testFriend.getId());
        likesDao.save(testFilmThird, testUser.getId());
        filmDao.save(testFilmOne);
        filmDao.save(testFilmSecond);
        filmDao.save(testFilmThird);

        List<Film> films = filmDao.findPopularFilms(2);
        Film bestPopularFilm = films.get(films.size() - 2);
        Film secondaryPopularFilm = films.get(films.size() - 1);

        assertThat(films.size()).isEqualTo(2);
        assertThat(bestPopularFilm).isNotNull();
        assertThat(bestPopularFilm.getName()).isEqualTo(testFilmSecond.getName());
        assertThat(secondaryPopularFilm).isNotNull();
        assertThat(secondaryPopularFilm.getName()).isEqualTo(testFilmThird.getName());
    }

    @Test
    public void filmRemoveInvalidLikeDeleteTest() {
        userDao.save(testUser);
        filmDao.save(testFilmOne);
        likesDao.save(testFilmOne, testUser.getId());
        likesDao.delete(testFilmOne, -1);
        List<User> likesUsers = likesDao.findLikesById(testFilmOne.getId());

        assertThat(likesDao.isContainsIdUserId(testFilmOne.getId(), testUser.getId())).isTrue();
        assertThat(likesUsers.size()).isEqualTo(1);
    }

    @Test
    public void filmGenresUpdateTest() {
        filmDao.save(testFilmOne);
        Film updateFilm = Film.builder()
                .id(1)
                .name("Офицеры")
                .description("Классика")
                .releaseDate(LocalDate.of(1971, Month.JULY, 26))
                .duration(97)
                .mpa(Optional.of(mpa5))
                .genres(List.of(genre1))
                .build();
        filmDao.update(updateFilm.getId(), updateFilm);

        Film savedUpdateFilm = filmDao.findById(1);

        assertThat(savedUpdateFilm).isNotNull();
        assertThat(savedUpdateFilm.getGenres()).isEqualTo(updateFilm.getGenres());
    }

    @Test
    public void filmInvalidGenresUpdateTest() {
        filmDao.save(testFilmOne);
        Genre genre = new Genre(-1, null);
        Film updateFilm = Film.builder()
                .id(1)
                .name("Офицеры")
                .description("Классика")
                .releaseDate(LocalDate.of(1971, Month.JULY, 26))
                .duration(97)
                .mpa(Optional.of(mpa5))
                .genres(List.of(genre))
                .build();
        try {
            filmDao.update(updateFilm.getId(), updateFilm);
        } catch (NoSuchElementException e) {
        }

        Film savedUpdateFilm = filmDao.findById(1);

        assertThat(savedUpdateFilm).isNotNull();
        assertThat(savedUpdateFilm.getGenres()).isEqualTo(testFilmOne.getGenres());
    }

    @Test
    public void filmRemoveGenresUpdateTest() {
        filmDao.save(testFilmOne);
        Film updateFilm = Film.builder()
                .id(1)
                .name("Офицеры")
                .description("Классика")
                .releaseDate(LocalDate.of(1971, Month.JULY, 26))
                .duration(97)
                .mpa(Optional.of(mpa5))
                .genres(List.of(genre1))
                .build();
        filmDao.update(updateFilm.getId(), updateFilm);

        updateFilm = Film.builder()
                .id(1)
                .name("Офицеры")
                .description("Классика")
                .releaseDate(LocalDate.of(1971, Month.JULY, 26))
                .duration(97)
                .mpa(Optional.of(mpa5))
                .genres(List.of())
                .build();
        filmDao.update(updateFilm.getId(), updateFilm);

        Film savedUpdateFilm = filmDao.findById(1);

        assertThat(savedUpdateFilm).isNotNull();
        assertThat(savedUpdateFilm.getGenres()).isEqualTo(testFilmOne.getGenres());
    }

    @Test
    public void filmNotExistingGenresTest() {
        filmDao.save(testFilmOne);
        Film savedFilm = filmDao.findById(1);

        assertThat(savedFilm).isNotNull();
        assertThat(savedFilm.getGenres()).isEqualTo(testFilmOne.getGenres());
    }

    @Test
    public void filmSecondGenresUpdateTest() {
        filmDao.save(testFilmOne);
        filmDao.save(testFilmSecond);
        Film updateFilm = Film.builder()
                .id(2)
                .name("Офицеры")
                .description("Классика")
                .releaseDate(LocalDate.of(1971, Month.JULY, 26))
                .duration(97)
                .mpa(Optional.of(mpa5))
                .genres(List.of(genre1, genre3, genre6))
                .build();
        filmDao.update(updateFilm.getId(), updateFilm);

        Film savedUpdateFilm = filmDao.findById(2);
        List<Genre> genres = savedUpdateFilm.getGenres();

        assertThat(savedUpdateFilm).isNotNull();
        assertThat(genres).isEqualTo(updateFilm.getGenres());
        assertThat(genres.get(0).getId()).isEqualTo(genre1.getId());
        assertThat(genres.get(1).getId()).isEqualTo(genre3.getId());
        assertThat(genres.get(2).getId()).isEqualTo(genre6.getId());
        assertThat(genres.get(0).getName()).isEqualTo(genre1.getName());
        assertThat(genres.get(1).getName()).isEqualTo(genre3.getName());
        assertThat(genres.get(2).getName()).isEqualTo(genre6.getName());
    }

    @Test
    public void filmWithDuplicatesGenresUpdateTest() {
        filmDao.save(testFilmOne);
        Film updateFilm = Film.builder()
                .id(1)
                .name("Офицеры")
                .description("Классика")
                .releaseDate(LocalDate.of(1971, Month.JULY, 26))
                .duration(97)
                .mpa(Optional.of(mpa5))
                .genres(List.of(genre1, genre3, genre1))
                .build();
        try {
            filmDao.update(updateFilm.getId(), updateFilm);
        } catch (DuplicateKeyException e) {
        }

        Film savedUpdateFilm = filmDao.findById(1);
        List<Genre> genres = savedUpdateFilm.getGenres();

        assertThat(savedUpdateFilm).isNotNull();
        assertThat(genres.size()).isEqualTo(2);
        assertThat(genres.get(0).getId()).isEqualTo(genre1.getId());
        assertThat(genres.get(1).getId()).isEqualTo(genre3.getId());
        assertThat(genres.get(0).getName()).isEqualTo(genre1.getName());
        assertThat(genres.get(1).getName()).isEqualTo(genre3.getName());
    }
}

