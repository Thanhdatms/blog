package com.group7.blog.dto.UserBlogVote.response;


import com.group7.blog.enums.EnumData.VoteType;
import com.group7.blog.models.Users;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ListUserVoteResponse {
    VoteType voteType;
    List<Users> user;

}
