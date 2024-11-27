package com.group7.blog.mappers;

import com.group7.blog.dto.Role.response.RoleResponse;
import com.group7.blog.models.Role;
import com.group7.blog.repositories.RoleRepository;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    RoleResponse toRoleResponse(Role role);
}
