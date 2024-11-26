package com.group7.blog.dto.User.request;

import com.group7.blog.models.Users;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PasswordResetTokenDTO {
    String token;
    Date expiryDate;
    Users user;
}
