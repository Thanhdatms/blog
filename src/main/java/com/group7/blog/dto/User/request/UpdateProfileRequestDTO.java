package com.group7.blog.dto.User.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateProfileRequestDTO {
    String nameTag;
    String username;
    String phoneNumber;
    String bio;
    String avatar;
}
