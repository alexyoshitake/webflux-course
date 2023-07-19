package br.com.yoshitake.webfluxcourse.controller.exceptions;

import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
public class StandadError implements Serializable {

	@Serial
	private static final long serialVersionUID = 4170784206759223187L;

	private LocalDateTime timestamp;
	private String path;
	private Integer status;
	private String error;
	private String message;

}
