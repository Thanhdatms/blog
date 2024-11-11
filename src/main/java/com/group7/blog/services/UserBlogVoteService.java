package com.group7.blog.services;

import com.group7.blog.dto.UserBlogVote.request.BlogVoteCreationRequest;
import com.group7.blog.dto.UserBlogVote.request.CountBlogVoteRequest;
import com.group7.blog.dto.UserBlogVote.response.BlogVoteResponse;
import com.group7.blog.dto.UserBlogVote.response.CountBlogVoteResponse;
import com.group7.blog.enums.EnumData.VoteType;
import com.group7.blog.enums.ErrorCode;
import com.group7.blog.exceptions.AppException;
import com.group7.blog.mappers.UserBlogVoteMapper;
import com.group7.blog.models.Blog;
import com.group7.blog.models.UserBlogVote;
import com.group7.blog.models.Users;
import com.group7.blog.repositories.BlogRepository;
import com.group7.blog.repositories.UserBlogVoteRepository;
import com.group7.blog.repositories.UserRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.EnumSet;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserBlogVoteService {

    UserRepository userRepository;
    BlogRepository blogRepository;
    UserBlogVoteRepository userBlogVoteRepository;
    UserBlogVoteMapper userBlogVoteMapper;
    public BlogVoteResponse createOrUpdateBlogVote(BlogVoteCreationRequest request){
        SecurityContext context = SecurityContextHolder.getContext();
        String userId = context.getAuthentication().getName();

        Users user = userRepository
                .findById(UUID
                        .fromString(userId))
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        Blog blog = blogRepository
                .findById(request.getBlog().getId())
                .orElseThrow(() -> new AppException(ErrorCode.BLOG_NOT_EXISTED));


        if (request.getVoteType() == null) {
            throw new AppException(ErrorCode.VOTETYPE_NOT_EXISTED);
        }

        if (!EnumSet.allOf(VoteType.class).contains(request.getVoteType())) {
            throw new AppException(ErrorCode.VOTETYPE_NOT_EXISTED);
        }

        Optional<UserBlogVote> existingVoteOpt = userBlogVoteRepository.findByUsersAndBlog(user, blog);
        if (existingVoteOpt.isPresent()){
            UserBlogVote existingVote = existingVoteOpt.get();
            // Same vote => undo

            if(existingVote.getVoteType() == request.getVoteType()){
                userBlogVoteRepository.delete(existingVote);
                return BlogVoteResponse.builder()
                        .id(existingVote.getId())
                        .blogId(blog.getId())
                        .userId(user.getId())
                        .voteType(existingVote.getVoteType())
                        .createdAt(new Timestamp(System.currentTimeMillis()))
                        .isRemove(true)
                        .build();
            }else {
                existingVote.setVoteType(request.getVoteType());
                userBlogVoteRepository.save(existingVote);
                return userBlogVoteMapper.toResponse(existingVote);
            }
        }else {
            // Create a new vote
            UserBlogVote newVote = userBlogVoteMapper.toUserBlogVote(request);
            newVote.setBlog(blog);
            newVote.setUsers(user);
            userBlogVoteRepository.save(newVote);
            return userBlogVoteMapper.toResponse(newVote);
        }
    }

    public CountBlogVoteResponse countBlogVote(CountBlogVoteRequest request) {
        Blog blog = blogRepository.findById(request.getBlog().getId())
                .orElseThrow(() -> new AppException(ErrorCode.BLOG_NOT_EXISTED));

        Long upvoteCount = userBlogVoteRepository.countByBlog_IdAndVoteType(blog.getId(), VoteType.UPVOTE);
        Long downvoteCount = userBlogVoteRepository.countByBlog_IdAndVoteType(blog.getId(), VoteType.DOWNVOTE);

        return CountBlogVoteResponse.builder()
                .blogId(blog.getId())
                .upvote(upvoteCount)
                .downvote(downvoteCount)
                .build();
    }
}
