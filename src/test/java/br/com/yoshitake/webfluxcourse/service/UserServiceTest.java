package br.com.yoshitake.webfluxcourse.service;

import br.com.yoshitake.webfluxcourse.entity.User;
import br.com.yoshitake.webfluxcourse.mapper.UserMapper;
import br.com.yoshitake.webfluxcourse.model.request.UserRequest;
import br.com.yoshitake.webfluxcourse.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

	@Mock
	private UserRepository repository;

	@Mock
	private UserMapper mapper;

	@InjectMocks
	private UserService service;

	@Test
	void testSave() {
		UserRequest request = new UserRequest("Alex", "alex.yoshitake@gmail.com", "123");
		User entity = User.builder().build();

		when(this.mapper.toEntity(any(UserRequest.class))).thenReturn(entity);
		when(this.repository.save(any(User.class))).thenReturn(Mono.just(User.builder().build()));

		Mono<User> result = this.service.save(request);

		StepVerifier.create(result)
				.expectNextMatches(user -> user.getClass() == User.class)
				.expectComplete()
				.verify();

		verify(this.repository, times(1)).save(any(User.class));
	}

	@Test
	void testFindById() {
		when(this.repository.findById(anyString())).thenReturn(Mono.just(User.builder().build()));

		Mono<User> result = this.service.findById("123");

		StepVerifier.create(result)
				.expectNextMatches(user -> user.getClass() == User.class)
				.expectComplete()
				.verify();

		verify(this.repository, times(1)).findById(anyString());
	}

	@Test
	void testFindAll() {
		when(this.repository.findAll()).thenReturn(Flux.just(User.builder().build()));

		Flux<User> result = this.service.findAll();

		StepVerifier.create(result)
				.expectNextMatches(user -> user.getClass() == User.class)
				.expectComplete()
				.verify();

		verify(this.repository, times(1)).findAll();
	}

}