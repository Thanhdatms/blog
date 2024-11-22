package com.group7.blog.mappers;

import com.group7.blog.dto.User.reponse.UserBlogResponse;
import com.group7.blog.dto.User.reponse.UserProfileResponseDTO;
import com.group7.blog.dto.User.reponse.UserResponse;
import com.group7.blog.dto.User.request.UpdateProfileRequestDTO;
import com.group7.blog.dto.User.request.UserCreationRequest;
import com.group7.blog.dto.User.request.UserUpdateRequest;
import com.group7.blog.models.Users;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "username", source = "username")
    Users toUser(UserCreationRequest request);


    void updateUser(@MappingTarget Users user, UserUpdateRequest request);

    UserBlogResponse toUserWithBlogResponse(Users user);

    @Mapping(source = "blogs", target = "blogs")
    UserResponse toUserResponse(Users user);

    @Mapping(source = "id", target = "id")
    UserProfileResponseDTO toUserProfileResponse(Users user);

    void updateUserProfile(@MappingTarget Users user, UpdateProfileRequestDTO request);
}
