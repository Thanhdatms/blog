package com.group7.blog.dto.User.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreationRequest {
    String nameTag;
    String username;
    String email;
    String password;
    String phoneNumber;
    String avatar;
}