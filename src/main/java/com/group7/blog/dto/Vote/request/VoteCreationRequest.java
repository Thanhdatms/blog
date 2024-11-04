package com.group7.blog.dto.Vote.request;


import com.group7.blog.models.Vote.VoteType;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VoteCreationRequest {
    UUID id;
    VoteType voteType;
}
