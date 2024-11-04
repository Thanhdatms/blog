package com.group7.blog.mappers;


import com.group7.blog.dto.Vote.request.VoteCreationRequest;
import com.group7.blog.dto.Vote.response.VoteResponse;
import com.group7.blog.models.UserBlogVote;
import com.group7.blog.models.Vote;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface VoteMapper {
    Vote toVote(VoteCreationRequest request);

    @Mapping(target = "voteType", source = "vote.voteType")
    VoteResponse toVoteResponse(Vote vote);
}
