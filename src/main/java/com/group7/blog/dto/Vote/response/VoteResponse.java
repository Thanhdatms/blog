package com.group7.blog.dto.Vote.response;


import lombok.*;
import lombok.experimental.FieldDefaults;
import com.group7.blog.models.Vote.VoteType;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VoteResponse {
    UUID id;
    String voteType;

}
