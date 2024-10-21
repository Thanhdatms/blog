package com.group7.blog.mappers;

import com.group7.blog.dto.Blog.request.BlogCreationRequest;
import com.group7.blog.dto.Blog.request.BlogUpdateRequest;
import com.group7.blog.dto.Blog.response.BlogDetailResponse;
import com.group7.blog.dto.Blog.response.BlogResponse;
import com.group7.blog.models.Blog;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface BlogMapper {
    Blog toBlog(BlogCreationRequest request);

    @Mapping(target = "title", source = "title")
    @Mapping(target = "content", source = "content")
    @Mapping(target = "status", source = "status")
    @Mapping(target = "publishedAt", source = "publishedAt")
    void updateBlog(@MappingTarget Blog blog, BlogUpdateRequest request);

    BlogResponse toBlogResponse(Blog blog);
    BlogDetailResponse toBlogDetailResponse(Blog blog);
}



