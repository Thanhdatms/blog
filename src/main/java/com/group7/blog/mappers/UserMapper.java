package com.group7.blog.mappers;

import com.group7.blog.dto.User.reponse.UserResponse;
import com.group7.blog.dto.User.request.UserCreationRequest;
import com.group7.blog.dto.User.request.UserUpdateRequest;
import com.group7.blog.models.Users;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "firstname", source = "firstname")
    @Mapping(target = "lastname", source = "lastname")
    @Mapping(target = "username", source = "username")
    @Mapping(target = "hashpassword", source = "hashpassword")
    Users toUser(UserCreationRequest request);
    @Mapping(target = "firstname", source = "firstname")
    @Mapping(target = "lastname", source = "lastname")
    @Mapping(target = "username", source = "username")
    @Mapping(target = "hashpassword", source = "hashpassword")
    UserResponse toUserResponse(Users user);
    void updateUser(@MappingTarget Users user, UserUpdateRequest request);

}
