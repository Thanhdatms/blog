package com.group7.blog.mappers;

import com.group7.blog.dto.Comment.request.CommentCreationRequest;
import com.group7.blog.dto.Comment.response.CommentResponse;
import com.group7.blog.models.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CommentMapper {


    @Mapping(target = "id", source = "comment.id")
    @Mapping(target = "blogId", source = "comment.blog.id")
    @Mapping(target = "parentId", source = "comment.parentComment.id")
    @Mapping(target = "comment", source = "comment.content")
    @Mapping(target = "createdAt", source = "comment.createdAt")
    CommentResponse toCommentResponse(Comment comment);

}
