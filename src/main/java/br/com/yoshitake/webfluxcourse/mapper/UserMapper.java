package br.com.yoshitake.webfluxcourse.mapper;

import br.com.yoshitake.webfluxcourse.entity.User;
import br.com.yoshitake.webfluxcourse.model.request.UserRequest;
import br.com.yoshitake.webfluxcourse.model.response.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import static org.mapstruct.NullValueCheckStrategy.ALWAYS;
import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;

@Mapper(
		componentModel = "spring",
		nullValuePropertyMappingStrategy = IGNORE,
		nullValueCheckStrategy = ALWAYS
)
public interface UserMapper {

	@Mapping(target = "id", ignore = true)
	User toEntity(final UserRequest resquest);

	@Mapping(target = "id", ignore = true)
	User toEntity(final UserRequest request, @MappingTarget final User entity);

	UserResponse toResponse(final User entity);
}
