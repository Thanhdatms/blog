package com.group7.blog.dto.UserBlogVote.response;


import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CountBlogVoteResponse {
    UUID blogId;
    Long upvote;
    Long downvote;
}
