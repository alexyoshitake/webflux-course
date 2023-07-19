package br.com.yoshitake.webfluxcourse.controller.exceptions;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Mono;

import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ControllerAdvice
public class ControllerExceptionHandler {

	@ExceptionHandler(DuplicateKeyException.class)
	ResponseEntity<Mono<StandadError>> duplicateKeyException(
			DuplicateKeyException ex, ServerHttpRequest request
	) {
		return ResponseEntity.badRequest()
				.body(Mono.just(
						StandadError.builder()
								.timestamp(now())
								.status(BAD_REQUEST.value())
								.error(BAD_REQUEST.getReasonPhrase())
								.message(this.verifyDupKey(ex.getMessage()))
								.path(request.getPath().toString())
								.build()
				));
	}

	@ExceptionHandler(WebExchangeBindException.class)
	ResponseEntity<Mono<ValidationError>> validationError(
			WebExchangeBindException ex,
			ServerHttpRequest request
	) {
		ValidationError error = new ValidationError(now(), request.getPath().toString(),
				BAD_REQUEST.value(), "Validation error", "Error on validation attributes");

		for (FieldError x : ex.getBindingResult().getFieldErrors()) {
			error.addError(x.getField(), x.getDefaultMessage());
		}
		return ResponseEntity.badRequest().body(Mono.just(error));
	}

	private String verifyDupKey(String message) {
		if (message.contains("email dup key")) {
			return "E-mail already registered";
		}
		return "Dup key exception";
	}

}
