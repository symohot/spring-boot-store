package com.codewithmosh.store.mappers;

import com.codewithmosh.store.dto.RegisterUserRequest;
import com.codewithmosh.store.dto.UpdateUserRequest;
import com.codewithmosh.store.dto.UserDto;
import com.codewithmosh.store.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target="createdAt", expression = "java(java.time.LocalDateTime.now())")
    UserDto toDto(User user);

    User toEntity(RegisterUserRequest registerUserRequest);

    void updateUser(UpdateUserRequest updateUserRequest, @MappingTarget User user);
}
