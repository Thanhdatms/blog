package com.group7.blog.controllers;


import com.group7.blog.dto.User.reponse.ApiResponse;
import com.group7.blog.dto.Vote.request.VoteCreationRequest;
import com.group7.blog.dto.Vote.response.VoteResponse;
import com.group7.blog.services.VoteService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/votes")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class VoteController {
    VoteService voteService;

    @PostMapping
    ApiResponse<VoteResponse> createBlogVote(@RequestBody VoteCreationRequest request){
        return ApiResponse.<VoteResponse>builder()
                .result(voteService.createVote(request))
                .build();
    }

    @GetMapping
    ApiResponse<List<VoteResponse>> getVotes(){
        return ApiResponse.<List<VoteResponse>>builder()
                .result(voteService.getVotes())
                .build();
    }

}
