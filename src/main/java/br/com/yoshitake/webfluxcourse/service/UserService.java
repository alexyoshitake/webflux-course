package br.com.yoshitake.webfluxcourse.service;

import br.com.yoshitake.webfluxcourse.entity.User;
import br.com.yoshitake.webfluxcourse.mapper.UserMapper;
import br.com.yoshitake.webfluxcourse.model.request.UserRequest;
import br.com.yoshitake.webfluxcourse.repository.UserRepository;
import br.com.yoshitake.webfluxcourse.service.exception.ObjectNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository repository;
	private final UserMapper mapper;

	public Mono<User> save(final UserRequest request) {
		return repository.save(mapper.toEntity(request));
	}

	public Mono<User> findById(final String id) {
		return this.repository.findById(id)
				.switchIfEmpty(Mono.error(
						new ObjectNotFoundException(
								String.format("Object not found. Id: %s, Type: %s", id, User.class.getSimpleName())
						)
				));
	}

	public Flux<User> findAll(){
		return this.repository.findAll();

	}

	public Mono<User> update(final String id, UserRequest request){
		return this.findById(id)
				.map(entity -> this.mapper.toEntity(request, entity))
				.flatMap(this.repository::save);
	}

}
