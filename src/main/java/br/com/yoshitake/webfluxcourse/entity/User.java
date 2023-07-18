package br.com.yoshitake.webfluxcourse.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document
public class User {

	@Id
	private String id;
	private String nome;

	@Indexed(unique = true)
	private String email;
	private String password;

}
