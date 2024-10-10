package com.group7.blog.dto.Blog.response;
import lombok.*;
import lombok.experimental.FieldDefaults;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BlogResponse {
    UUID id;
    String title;
    String content;
    String summary;
    String thumbnail;
    boolean status;
}
