package com.group7.blog.controllers;


import com.group7.blog.dto.User.reponse.ApiResponse;
import com.group7.blog.dto.UserBlogVote.request.BlogVoteCreationRequest;
import com.group7.blog.dto.UserBlogVote.request.CountBlogVoteRequest;
import com.group7.blog.dto.UserBlogVote.response.BlogVoteResponse;
import com.group7.blog.dto.UserBlogVote.response.CountBlogVoteResponse;
import com.group7.blog.services.UserBlogVoteService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/blogvote")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserBlogVoteController {
    UserBlogVoteService userBlogVoteService;
    @PostMapping
    ApiResponse<BlogVoteResponse> createBlogVote(@RequestBody BlogVoteCreationRequest request){
        return ApiResponse.<BlogVoteResponse>builder()
                .result(userBlogVoteService.createOrUpdateBlogVote(request))
                .build();
    }

    @GetMapping("/count")
    ApiResponse<CountBlogVoteResponse> countBlogVote(@RequestBody CountBlogVoteRequest request){
        return ApiResponse.<CountBlogVoteResponse>builder()
                .result(userBlogVoteService.countBlogVote(request))
                .build();
    }
}
