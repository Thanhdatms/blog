package com.group7.blog.services;
import com.group7.blog.dto.Vote.request.VoteCreationRequest;
import com.group7.blog.dto.Vote.response.VoteResponse;
import com.group7.blog.models.Vote;
import com.group7.blog.repositories.VoteRepository;
import com.group7.blog.mappers.VoteMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class VoteService {
    VoteRepository voteRepository;
    VoteMapper voteMapper;
    public VoteResponse createVote(VoteCreationRequest request) {
        Vote vote = voteMapper.toVote(request);
        return voteMapper.toVoteResponse(voteRepository.save(vote));
    }

    public List<VoteResponse> getVotes() {
        return voteRepository.findAll()
                .stream()
                .map(voteMapper::toVoteResponse)
                .collect(Collectors.toList());
    }
}
