package ru.yandex.practicum.filmorate;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.MethodArgumentNotValidException;
import ru.yandex.practicum.filmorate.controller.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class FilmorateApplicationTests {
	private static final String PATH_FILMS = "/films";
	private static final String PATH_USERS = "/users";
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void ValidFilmValidationTest() throws Exception {
		Film validFilm =
				new Film(null, "TestFilmName", "Test".repeat(50), LocalDate.now(), 99);
		Film validFilmExcepted =
				new Film(1, "TestFilmName", "Test".repeat(50), LocalDate.now(), 99);
		mockMvc.perform(post(PATH_FILMS)
				.content(objectMapper.writeValueAsString(validFilm))
				.contentType(MediaType.APPLICATION_JSON))
					.andExpect(status().isCreated())
					.andExpect(content().json(objectMapper.writeValueAsString(validFilmExcepted)));

		Film editValidFilm =
				new Film(1, "TestFilmName edit", "Edit".repeat(50), LocalDate.of(2022, Month.AUGUST, 11), 111);
		Film editValidFilmExcepted =
				new Film(1, "TestFilmName edit", "Edit".repeat(50), LocalDate.of(2022, Month.AUGUST, 11), 111);
		mockMvc.perform(put(PATH_FILMS)
						.content(objectMapper.writeValueAsString(editValidFilm))
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().json(objectMapper.writeValueAsString(editValidFilmExcepted)));

		mockMvc.perform(get(PATH_FILMS))
				.andExpect(status().isOk())
				.andExpect(content().json(objectMapper.writeValueAsString(List.of(editValidFilmExcepted))));
	}

	@Test
	void InvalidFilmValidationTest() throws Exception {
		mockMvc.perform(post(PATH_FILMS)
						.content(objectMapper.writeValueAsString(null))
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(mvcResult ->
						assertTrue(mvcResult.getResolvedException() instanceof HttpMessageNotReadableException));
		mockMvc.perform(put(PATH_FILMS)
						.content(objectMapper.writeValueAsString(null))
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(mvcResult ->
						assertTrue(mvcResult.getResolvedException() instanceof HttpMessageNotReadableException));

		Film invalidFilmWithoutId =
				new Film(null, "TestFilmName", "TestFilmDescription", LocalDate.now(), 99);
		mockMvc.perform(put(PATH_FILMS)
						.content(objectMapper.writeValueAsString(invalidFilmWithoutId))
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(mvcResult ->
						assertTrue(mvcResult.getResolvedException() instanceof ValidationException));

		Film invalidFilmWithNonExistentId =
				new Film(999, "TestFilmName", "TestFilmDescription", LocalDate.now(), 99);
		mockMvc.perform(put(PATH_FILMS)
						.content(objectMapper.writeValueAsString(invalidFilmWithNonExistentId))
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(mvcResult ->
						assertTrue(mvcResult.getResolvedException() instanceof ValidationException));

		Film invalidFilmWithoutName =
				new Film(null, null, "TestFilmDescription", LocalDate.now(), 99);
		mockMvc.perform(post(PATH_FILMS)
						.content(objectMapper.writeValueAsString(invalidFilmWithoutName))
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(mvcResult ->
						assertTrue(mvcResult.getResolvedException() instanceof MethodArgumentNotValidException));
		mockMvc.perform(put(PATH_FILMS)
						.content(objectMapper.writeValueAsString(invalidFilmWithoutName))
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(mvcResult ->
						assertTrue(mvcResult.getResolvedException() instanceof MethodArgumentNotValidException));

		Film invalidFilmWithEmptyName =
				new Film(null, "", "TestFilmDescription", LocalDate.now(), 99);
		mockMvc.perform(post(PATH_FILMS)
						.content(objectMapper.writeValueAsString(invalidFilmWithEmptyName))
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(mvcResult ->
						assertTrue(mvcResult.getResolvedException() instanceof MethodArgumentNotValidException));
		mockMvc.perform(put(PATH_FILMS)
						.content(objectMapper.writeValueAsString(invalidFilmWithEmptyName))
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(mvcResult ->
						assertTrue(mvcResult.getResolvedException() instanceof MethodArgumentNotValidException));

		Film invalidFilmWithNameOverLimit =
				new Film(null, "TestFilmName", "X".repeat(201), LocalDate.now(), 99);
		mockMvc.perform(post(PATH_FILMS)
						.content(objectMapper.writeValueAsString(invalidFilmWithNameOverLimit))
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(mvcResult ->
						assertTrue(mvcResult.getResolvedException() instanceof MethodArgumentNotValidException));
		invalidFilmWithNameOverLimit =
				new Film(0, "TestFilmName", "X".repeat(201), LocalDate.now(), 99);
		mockMvc.perform(put(PATH_FILMS)
						.content(objectMapper.writeValueAsString(invalidFilmWithNameOverLimit))
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(mvcResult ->
						assertTrue(mvcResult.getResolvedException() instanceof MethodArgumentNotValidException));

		Film invalidFilmWithReleaseDateBeforeEraOfCinema =
				new Film(null, "TestFilmName", "TestFilmDescription", LocalDate.of(1895, Month.DECEMBER, 27), 99);
		mockMvc.perform(post(PATH_FILMS)
						.content(objectMapper.writeValueAsString(invalidFilmWithReleaseDateBeforeEraOfCinema))
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(mvcResult ->
						assertTrue(mvcResult.getResolvedException() instanceof ValidationException));
		invalidFilmWithReleaseDateBeforeEraOfCinema =
				new Film(0, "TestFilmName", "TestFilmDescription", LocalDate.of(1895, Month.DECEMBER, 27), 99);
		mockMvc.perform(put(PATH_FILMS)
						.content(objectMapper.writeValueAsString(invalidFilmWithReleaseDateBeforeEraOfCinema))
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(mvcResult ->
						assertTrue(mvcResult.getResolvedException() instanceof ValidationException));

		Film invalidFilmWithNegativeDuration =
				new Film(null, "TestFilmName", "TestFilmDescription", LocalDate.now(), -1);
		mockMvc.perform(post(PATH_FILMS)
						.content(objectMapper.writeValueAsString(invalidFilmWithNegativeDuration))
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(mvcResult ->
						assertTrue(mvcResult.getResolvedException() instanceof MethodArgumentNotValidException));
		invalidFilmWithNegativeDuration =
				new Film(0, "TestFilmName", "TestFilmDescription", LocalDate.now(), -1);
		mockMvc.perform(put(PATH_FILMS)
						.content(objectMapper.writeValueAsString(invalidFilmWithNegativeDuration))
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(mvcResult ->
						assertTrue(mvcResult.getResolvedException() instanceof MethodArgumentNotValidException));
	}

	@Test
	void ValidUserValidationTest() throws Exception {
		User validUser = new User(null, "test@test.ru", "TestLogin", "TestName", LocalDate.now());
		User validUserExcepted = new User(1, "test@test.ru", "TestLogin", "TestName", LocalDate.now());
		mockMvc.perform(post(PATH_USERS)
						.content(objectMapper.writeValueAsString(validUser))
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andExpect(content().json(objectMapper.writeValueAsString(validUserExcepted)));

		User editValidUser = new User(1, "edit@edit.com", "TestLoginEdit", "", LocalDate.of(2010, Month.MAY, 31));
		User editValidUserExcepted =
				new User(1, "edit@edit.com", "TestLoginEdit", "TestLoginEdit", LocalDate.of(2010, Month.MAY, 31));
		mockMvc.perform(put(PATH_USERS)
						.content(objectMapper.writeValueAsString(editValidUser))
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().json(objectMapper.writeValueAsString(editValidUserExcepted)));

		mockMvc.perform(get(PATH_USERS))
				.andExpect(status().isOk())
				.andExpect(content().json(objectMapper.writeValueAsString(List.of(editValidUserExcepted))));
	}

	@Test
	void InvalidUserValidationTest() throws Exception {
		mockMvc.perform(post(PATH_USERS)
						.content(objectMapper.writeValueAsString(null))
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(mvcResult ->
						assertTrue(mvcResult.getResolvedException() instanceof HttpMessageNotReadableException));
		mockMvc.perform(put(PATH_USERS)
						.content(objectMapper.writeValueAsString(null))
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(mvcResult ->
						assertTrue(mvcResult.getResolvedException() instanceof HttpMessageNotReadableException));

		User invalidUserWithoutId =
				new User(null, "test@test.ru", "TestLogin", "TestName", LocalDate.now());
		mockMvc.perform(put(PATH_USERS)
						.content(objectMapper.writeValueAsString(invalidUserWithoutId))
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(mvcResult ->
						assertTrue(mvcResult.getResolvedException() instanceof ValidationException));

		User invalidUserWithNonExistentId =
				new User(999, "test@test.ru", "TestLogin", "TestName", LocalDate.now());
		mockMvc.perform(put(PATH_USERS)
						.content(objectMapper.writeValueAsString(invalidUserWithNonExistentId))
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(mvcResult ->
						assertTrue(mvcResult.getResolvedException() instanceof ValidationException));

		User invalidUserWithoutEmail =
				new User(null, null, "TestLogin", "TestName", LocalDate.now());
		mockMvc.perform(post(PATH_USERS)
						.content(objectMapper.writeValueAsString(invalidUserWithoutEmail))
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(mvcResult ->
						assertTrue(mvcResult.getResolvedException() instanceof MethodArgumentNotValidException));
		invalidUserWithoutEmail =
				new User(0, null, "TestLogin", "TestName", LocalDate.now());
		mockMvc.perform(put(PATH_USERS)
						.content(objectMapper.writeValueAsString(invalidUserWithoutEmail))
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(mvcResult ->
						assertTrue(mvcResult.getResolvedException() instanceof MethodArgumentNotValidException));

		User invalidUserWithEmptyEmail =
				new User(null, "", "TestLogin", "TestName", LocalDate.now());
		mockMvc.perform(post(PATH_USERS)
						.content(objectMapper.writeValueAsString(invalidUserWithEmptyEmail))
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(mvcResult ->
						assertTrue(mvcResult.getResolvedException() instanceof MethodArgumentNotValidException));
		invalidUserWithEmptyEmail =
				new User(0, "", "TestLogin", "TestName", LocalDate.now());
		mockMvc.perform(put(PATH_USERS)
						.content(objectMapper.writeValueAsString(invalidUserWithEmptyEmail))
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(mvcResult ->
						assertTrue(mvcResult.getResolvedException() instanceof MethodArgumentNotValidException));

		User invalidUserWithInvalidEmail =
				new User(null, "InvalidEmail", "TestLogin", "TestName", LocalDate.now());
		mockMvc.perform(post(PATH_USERS)
						.content(objectMapper.writeValueAsString(invalidUserWithInvalidEmail))
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(mvcResult ->
						assertTrue(mvcResult.getResolvedException() instanceof MethodArgumentNotValidException));
		invalidUserWithInvalidEmail =
				new User(0, "InvalidEmail", "TestLogin", "TestName", LocalDate.now());
		mockMvc.perform(put(PATH_USERS)
						.content(objectMapper.writeValueAsString(invalidUserWithInvalidEmail))
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(mvcResult ->
						assertTrue(mvcResult.getResolvedException() instanceof MethodArgumentNotValidException));

		User invalidUserWithoutLogin =
				new User(null, "test@test.ru", null, "TestName", LocalDate.now());
		mockMvc.perform(post(PATH_USERS)
						.content(objectMapper.writeValueAsString(invalidUserWithoutLogin))
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(mvcResult ->
						assertTrue(mvcResult.getResolvedException() instanceof MethodArgumentNotValidException));
		invalidUserWithoutLogin =
				new User(0, "test@test.ru", null, "TestName", LocalDate.now());
		mockMvc.perform(put(PATH_USERS)
						.content(objectMapper.writeValueAsString(invalidUserWithoutLogin))
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(mvcResult ->
						assertTrue(mvcResult.getResolvedException() instanceof MethodArgumentNotValidException));

		User invalidUserWithEmptyLogin =
				new User(null, "test@test.ru", "", "TestName", LocalDate.now());
		mockMvc.perform(post(PATH_USERS)
						.content(objectMapper.writeValueAsString(invalidUserWithEmptyLogin))
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(mvcResult ->
						assertTrue(mvcResult.getResolvedException() instanceof MethodArgumentNotValidException));
		invalidUserWithEmptyLogin =
				new User(0, "test@test.ru", "", "TestName", LocalDate.now());
		mockMvc.perform(put(PATH_USERS)
						.content(objectMapper.writeValueAsString(invalidUserWithEmptyLogin))
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(mvcResult ->
						assertTrue(mvcResult.getResolvedException() instanceof MethodArgumentNotValidException));

		User invalidUserWithLoginContainingSpace =
				new User(null, "test@test.ru", "Test Login", "TestName", LocalDate.now());
		mockMvc.perform(post(PATH_USERS)
						.content(objectMapper.writeValueAsString(invalidUserWithLoginContainingSpace))
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(mvcResult ->
						assertTrue(mvcResult.getResolvedException() instanceof ValidationException));
		invalidUserWithLoginContainingSpace =
				new User(0, "test@test.ru", "Test Login", "TestName", LocalDate.now());
		mockMvc.perform(put(PATH_USERS)
						.content(objectMapper.writeValueAsString(invalidUserWithLoginContainingSpace))
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(mvcResult ->
						assertTrue(mvcResult.getResolvedException() instanceof ValidationException));

		User invalidUserWithBirthdayInFuture =
				new User(null, "test@test.ru", "TestLogin", "TestName", LocalDate.now().plusDays(1));
		mockMvc.perform(post(PATH_USERS)
						.content(objectMapper.writeValueAsString(invalidUserWithBirthdayInFuture))
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(mvcResult ->
						assertTrue(mvcResult.getResolvedException() instanceof MethodArgumentNotValidException));
		invalidUserWithBirthdayInFuture =
				new User(0, "test@test.ru", "TestLogin", "TestName", LocalDate.now().plusDays(1));
		mockMvc.perform(put(PATH_USERS)
						.content(objectMapper.writeValueAsString(invalidUserWithBirthdayInFuture))
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(mvcResult ->
						assertTrue(mvcResult.getResolvedException() instanceof MethodArgumentNotValidException));
	}

}
