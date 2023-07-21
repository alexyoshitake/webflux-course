package br.com.yoshitake.webfluxcourse.controller.impl;

import br.com.yoshitake.webfluxcourse.controller.UserController;
import br.com.yoshitake.webfluxcourse.mapper.UserMapper;
import br.com.yoshitake.webfluxcourse.model.request.UserRequest;
import br.com.yoshitake.webfluxcourse.model.response.UserResponse;
import br.com.yoshitake.webfluxcourse.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserControllerImpl implements UserController {

	private final UserService service;
	private final UserMapper mapper;

	@Override
	public ResponseEntity<Mono<Void>> save(final UserRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(this.service.save(request).then());
	}

	@Override
	public ResponseEntity<Mono<UserResponse>> findById(String id) {
		return ResponseEntity.ok().body(
				this.service.findById(id).map(this.mapper::toResponse)
		);
	}

	@Override
	public ResponseEntity<Flux<UserResponse>> findAll() {
		return ResponseEntity.ok().body(
				this.service.findAll().map(this.mapper::toResponse)
		);
	}

	@Override
	public ResponseEntity<Mono<UserResponse>> update(String id, UserRequest request) {
		return ResponseEntity.ok().body(
				this.service.update(id, request).map(this.mapper::toResponse)
		);
	}

	@Override
	public ResponseEntity<Mono<Void>> delete(String id) {
		return ResponseEntity.ok().body(
				this.service.delete(id).then()
		);
	}
}
