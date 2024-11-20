package com.group7.blog.dto.Comment.request;


import com.group7.blog.models.Blog;
import com.group7.blog.models.Comment;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentCreationRequest {
    UUID parentId;

    @NotNull
    @Size(min = 1, max = 500)
    String content;
}