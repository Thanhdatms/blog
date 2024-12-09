package com.group7.blog.dto.User.reponse;

import com.group7.blog.dto.Role.response.RoleResponse;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserProfileResponse {
    String id;
    String username;
    String nameTag;
    String bio;
    String avatar;
    List<RoleResponse> roles;
}
