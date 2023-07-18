package br.com.yoshitake.webfluxcourse.model.request;

public record UserRequest(
		String name,
		String email,
		String password
) { }
