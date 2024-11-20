package com.group7.blog.dto.Comment.response;

import com.group7.blog.dto.User.reponse.UserCommentResponse;
import com.group7.blog.dto.User.reponse.UserResponse;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentDetailResponse {
    UUID id;
    String content;
    UUID parentId;
    boolean isUpdate;
    UserCommentResponse user;
    List<CommentDetailResponse> children;
}
