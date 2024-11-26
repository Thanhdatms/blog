package com.group7.blog.services;


import com.group7.blog.dto.Comment.request.CommentCreationRequest;
import com.group7.blog.dto.Comment.response.CommentDetailResponse;
import com.group7.blog.dto.Comment.response.CommentResponse;
import com.group7.blog.dto.Comment.response.CommentStatusResponse;
import com.group7.blog.dto.User.reponse.UserInfoResponse;
import com.group7.blog.enums.ErrorCode;
import com.group7.blog.exceptions.AppException;
import com.group7.blog.mappers.CommentMapper;
import com.group7.blog.models.Blog;
import com.group7.blog.models.Comment;
import com.group7.blog.models.Users;
import com.group7.blog.repositories.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CommentService {
    BlogRepository blogRepository;
    UserRepository userRepository;
    CommentRepository commentRepository;
    CommentMapper commentMapper;
    public CommentResponse createComment(UUID blogId, CommentCreationRequest request){
        SecurityContext context = SecurityContextHolder.getContext();
        String userId = context.getAuthentication().getName();

        Users user = userRepository.findById(UUID.fromString(userId))
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        Blog blog = blogRepository.findById(blogId)
                .orElseThrow(() -> new AppException(ErrorCode.BLOG_NOT_EXISTED));

        Comment parentComment = Optional.ofNullable(request.getParentId())
                .map(parentId -> commentRepository.findById(parentId)
                        .orElseThrow(() -> new AppException(ErrorCode.PARENT_COMMENT_NOT_EXISTED)))
                .orElse(null);

        Comment newComment = new Comment();
        newComment.setContent(request.getContent());
        newComment.setBlog(blog);
        newComment.setUsers(user);
        newComment.setParentComment(parentComment);

        commentRepository.save(newComment);

        return commentMapper.toCommentResponse(newComment);

    }

    public CommentStatusResponse deleteComment(String blogId, String commentId){
        SecurityContext context = SecurityContextHolder.getContext();
        String userId = context.getAuthentication().getName();
        Users user = userRepository.findById(UUID
                .fromString(userId))
                .orElseThrow(() -> new AppException(ErrorCode.BLOG_NOT_EXISTED));

        Blog blog = blogRepository.findById(UUID.fromString(blogId))
                .orElseThrow(() -> new AppException(ErrorCode.BLOG_NOT_EXISTED));


        Comment comment = commentRepository.findById(UUID.fromString(commentId))
                .orElseThrow(() -> new AppException(ErrorCode.COMMENT_NOT_FOUND));

        // Check the comment in this blog
        if(!comment.getBlog().getId().equals(blog.getId())){
            throw new AppException(ErrorCode.COMMENT_NOT_BELONG_TO_BLOG);
        }
        // Check the user is the owner of comment or the owner of blog
        if (!comment.getUsers().getId().equals(UUID.fromString(userId)) &&
                !blog.getUsers().getId().equals(UUID.fromString(userId))){
            throw new AppException(ErrorCode.COMMENT_DELETE_PERMISSION_ERROR);
        }
        
        commentRepository.delete(comment);

        return new CommentStatusResponse(comment.getId(), "Delete comment successfully");
    }

    public CommentStatusResponse updateComment(String blogId, String commentId, String content){
        SecurityContext context = SecurityContextHolder.getContext();
        String userId = context.getAuthentication().getName();

        Users user = userRepository.findById(UUID
                        .fromString(userId))
                .orElseThrow(() -> new AppException(ErrorCode.BLOG_NOT_EXISTED));

        Blog blog = blogRepository.findById(UUID.fromString(blogId))
                .orElseThrow(() -> new AppException(ErrorCode.BLOG_NOT_EXISTED));


        Comment comment = commentRepository.findById(UUID.fromString(commentId))
                .orElseThrow(() -> new AppException(ErrorCode.COMMENT_NOT_FOUND));

        // Check the comment in this blog
        if(!comment.getBlog().getId().equals(blog.getId())){
            throw new AppException(ErrorCode.COMMENT_NOT_BELONG_TO_BLOG);
        }
        // Check the user is the owner of comment or the owner of blog
        if (!comment.getUsers().getId().equals(UUID.fromString(userId)) &&
                !blog.getUsers().getId().equals(UUID.fromString(userId))){
            throw new AppException(ErrorCode.COMMENT_DELETE_PERMISSION_ERROR);
        }


        comment.setContent(content);
        comment.setUpdate(true);
        commentRepository.save(comment);

        return new CommentStatusResponse(comment.getId(), "Update comment successfully");
    }

    public List<CommentDetailResponse> getPaginatedBlogComment(String blogId, int page, int size){
        SecurityContext context = SecurityContextHolder.getContext();
        String userId = context.getAuthentication().getName();

        Users user = userRepository.findById(UUID
                        .fromString(userId))
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        Blog blog = blogRepository.findById(UUID.fromString(blogId))
                .orElseThrow(() -> new AppException(ErrorCode.BLOG_NOT_EXISTED));


        Pageable pageable = PageRequest.of(page, size);

        Page<Comment> topLevelComments = commentRepository.findByBlogAndParentCommentIsNull(blog,pageable);

        return topLevelComments.stream()
                .map(this::mapToCommentDetailResponse)
                .collect(Collectors.toList());
    }

    private CommentDetailResponse mapToCommentDetailResponse(Comment comment){
        CommentDetailResponse response = new CommentDetailResponse();

        response.setId(comment.getId());
        response.setContent(comment.getContent());
        response.setParentId(comment.getParentComment() != null ?
                comment.getParentComment().getId() : null);
        response.setUpdate(comment.isUpdate());

        UserInfoResponse user = new UserInfoResponse();
        user.setId(comment.getUsers().getId());
        user.setUsername(comment.getUsers().getUsername());
        user.setAvatar(comment.getUsers().getAvatar());

        response.setUser(user);

        List<CommentDetailResponse> childResponse = comment.getComments().stream()
                .map(this::mapToCommentDetailResponse)
                .collect(Collectors.toList());
        response.setChildren(childResponse);

        return response;
    }
}
