package com.group7.blog.mapper;

import com.group7.blog.dto.request.UserCreationRequest;
import com.group7.blog.models.Users;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    Users toUser(UserCreationRequest request);
}
