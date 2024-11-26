package com.group7.blog.mappers;

import com.group7.blog.dto.User.reponse.UserBlogResponse;
import com.group7.blog.dto.User.reponse.UserProfileResponseDTO;
import com.group7.blog.dto.User.reponse.UserResponse;
import com.group7.blog.dto.User.request.UpdateProfileRequestDTO;
import com.group7.blog.dto.User.request.UserCreationRequest;
import com.group7.blog.dto.User.request.UserUpdateRequest;
import com.group7.blog.dto.UserBlogVote.response.VoteResponse;
import com.group7.blog.enums.EnumData;
import com.group7.blog.models.UserBlogVote;
import com.group7.blog.models.UserFollow;
import com.group7.blog.models.Users;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;

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
