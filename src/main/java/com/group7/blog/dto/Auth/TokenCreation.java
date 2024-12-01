package com.group7.blog.dto.Auth;

import com.group7.blog.dto.Role.response.RoleResponse;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TokenCreation {
    UUID userId;
    String username;
    List<RoleResponse> roles;
}
