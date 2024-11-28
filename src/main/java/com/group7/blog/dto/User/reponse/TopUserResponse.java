package com.group7.blog.dto.User.reponse;


import com.group7.blog.models.Users;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TopUserResponse {
    private UserResponse userResponse;
//    private int weeklyBlogCount;
//    private int totalUpvotes;
//    private int totalDownvotes;
    private double score;
}
