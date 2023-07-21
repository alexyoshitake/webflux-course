package br.com.yoshitake.webfluxcourse.controller;

import br.com.yoshitake.webfluxcourse.entity.User;
import br.com.yoshitake.webfluxcourse.mapper.UserMapper;
import br.com.yoshitake.webfluxcourse.model.request.UserRequest;
import br.com.yoshitake.webfluxcourse.model.response.UserResponse;
import br.com.yoshitake.webfluxcourse.service.UserService;
import com.mongodb.reactivestreams.client.MongoClient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.BodyInserters.fromValue;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
@AutoConfigureWebTestClient
class UserControllerImplTest {

	private static final String ID = "123456";
	private static final String NAME = "Alex";
	private static final String EMAIL = "alex.yoshitake@gmail.com";
	private static final String PASSWORD = "123";
	private static final String BASE_URI = "/users";

	@Autowired
	private WebTestClient webTestClient;

	@MockBean
	private UserService service;

	@MockBean
	private UserMapper mapper;

	@MockBean
	private MongoClient mongoClient;

	@Test
	@DisplayName("Teste endpoint save with success")
	void testSaveWithSuccess() {
		final var request = new UserRequest(NAME, EMAIL, PASSWORD);
		when(this.service.save(any(UserRequest.class))).thenReturn(Mono.just(User.builder().build()));

		this.webTestClient.post().uri(BASE_URI)
				.contentType(APPLICATION_JSON)
				.body(fromValue(request))
				.exchange()
				.expectStatus().isCreated();

		verify(this.service).save(any(UserRequest.class));
	}

	@Test
	@DisplayName("Teste endpoint save with bad request")
	void testSaveWithBadRequest() {
		final var request = new UserRequest(NAME.concat(" "), EMAIL, PASSWORD);

		this.webTestClient.post().uri(BASE_URI)
				.contentType(APPLICATION_JSON)
				.body(fromValue(request))
				.exchange()
				.expectStatus().isBadRequest()
				.expectBody()
				.jsonPath("$.path").isEqualTo(BASE_URI)
				.jsonPath("$.status").isEqualTo(HttpStatus.BAD_REQUEST.value())
				.jsonPath("$.error").isEqualTo("Validation error")
				.jsonPath("$.message").isEqualTo("Error on validation attributes")
				.jsonPath("$.errors[0].fieldName").isEqualTo("name")
				.jsonPath("$.errors[0].message").isEqualTo("field cannot have blank spaces at the beginning or at end");

		verifyNoInteractions(this.service);
	}

	@Test
	@DisplayName("Test find by id endpoint with success")
	void testFindByIdWithSuccess() {
		final var userResponse = new UserResponse(ID, NAME, EMAIL, PASSWORD);

		when(this.service.findById(anyString())).thenReturn(Mono.just(User.builder().build()));
		when(this.mapper.toResponse(any(User.class))).thenReturn(userResponse);

		this.webTestClient.get().uri((BASE_URI + "/").concat(ID))
				.accept(APPLICATION_JSON)
				.exchange()
				.expectStatus().isOk()
				.expectBody()
				.jsonPath("$.id").isEqualTo(ID)
				.jsonPath("$.name").isEqualTo(NAME)
				.jsonPath("$.email").isEqualTo(EMAIL)
				.jsonPath("$.password").isEqualTo(PASSWORD);

		verify(this.service).findById(anyString());
		verify(this.mapper).toResponse(any(User.class));
	}

	@Test
	@DisplayName("Test find all endpoint with success")
	void testFindAllWithSuccess() {
		final var userResponse = new UserResponse(ID, NAME, EMAIL, PASSWORD);

		when(this.service.findAll()).thenReturn(Flux.just(User.builder().build()));
		when(this.mapper.toResponse(any(User.class))).thenReturn(userResponse);

		this.webTestClient.get().uri(BASE_URI)
				.accept(APPLICATION_JSON)
				.exchange()
				.expectStatus().isOk()
				.expectBody()
				.jsonPath("$.[0].id").isEqualTo(ID)
				.jsonPath("$.[0].name").isEqualTo(NAME)
				.jsonPath("$.[0].email").isEqualTo(EMAIL)
				.jsonPath("$.[0].password").isEqualTo(PASSWORD);

		verify(this.service).findAll();
		verify(this.mapper).toResponse(any(User.class));
	}

	@Test
	@DisplayName("Test update endpoint with success")
	void testUpdateWithSuccess() {
		final var request = new UserRequest(NAME, EMAIL, PASSWORD);
		final var userResponse = new UserResponse(ID, NAME, EMAIL, PASSWORD);

		when(this.service.update(anyString(), any(UserRequest.class))).thenReturn(Mono.just(User.builder().build()));
		when(this.mapper.toResponse(any(User.class))).thenReturn(userResponse);

		this.webTestClient.patch().uri((BASE_URI + "/").concat(ID))
				.contentType(APPLICATION_JSON)
				.body(fromValue(request))
				.exchange()
				.expectStatus().isOk()
				.expectBody()
				.jsonPath("$.id").isEqualTo(ID)
				.jsonPath("$.name").isEqualTo(NAME)
				.jsonPath("$.email").isEqualTo(EMAIL)
				.jsonPath("$.password").isEqualTo(PASSWORD);

		verify(this.service).update(anyString(), any(UserRequest.class));
		verify(this.mapper).toResponse(any(User.class));
	}

	@Test
	@DisplayName("Test delete endpoint with success")
	void testDeleteWithSuccess() {
		when(this.service.delete(anyString())).thenReturn(Mono.just(User.builder().build()));

		this.webTestClient.delete().uri((BASE_URI + "/").concat(ID))
				.exchange()
				.expectStatus().isOk();

		verify(this.service).delete(anyString());
	}

}