package com.group7.blog.dto.BookMark.response;

import com.group7.blog.models.Blog;
import com.group7.blog.models.Users;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookMarkResponse {
    Blog blog;
    Users user;
}
