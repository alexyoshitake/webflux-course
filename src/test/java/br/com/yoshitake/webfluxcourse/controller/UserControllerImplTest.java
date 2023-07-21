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
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.BodyInserters.fromValue;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
@AutoConfigureWebTestClient
class UserControllerImplTest {

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
		final var request = new UserRequest("Alex", "alex.yoshitake@gmail.com", "123");
		when(this.service.save(any(UserRequest.class))).thenReturn(Mono.just(User.builder().build()));

		this.webTestClient.post().uri("/users")
				.contentType(APPLICATION_JSON)
				.body(fromValue(request))
				.exchange()
				.expectStatus().isCreated();

		verify(this.service, times(1)).save(any(UserRequest.class));
	}

	@Test
	@DisplayName("Teste endpoint save with bad request")
	void testSaveWithBadRequest() {
		final var request = new UserRequest(" Alex", "alex.yoshitake@gmail.com", "123");

		this.webTestClient.post().uri("/users")
				.contentType(APPLICATION_JSON)
				.body(fromValue(request))
				.exchange()
				.expectStatus().isBadRequest()
				.expectBody()
				.jsonPath("$.path").isEqualTo("/users")
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
		final var id = "123456";
		final var userResponse = new UserResponse(id, "Alex", "alex.yoshitake@gmail.com", "123");

		when(this.service.findById(anyString())).thenReturn(Mono.just(User.builder().build()));
		when(this.mapper.toResponse(any(User.class))).thenReturn(userResponse);

		this.webTestClient.get().uri("/users/".concat(id))
				.accept(APPLICATION_JSON)
				.exchange()
				.expectStatus().isOk()
				.expectBody()
				.jsonPath("$.id").isEqualTo(id)
				.jsonPath("$.name").isEqualTo("Alex")
				.jsonPath("$.email").isEqualTo("alex.yoshitake@gmail.com")
				.jsonPath("$.password").isEqualTo("123");
	}

	@Test
	void findAll() {
	}

	@Test
	void update() {
	}

	@Test
	void delete() {
	}

}