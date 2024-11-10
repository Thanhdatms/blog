package com.group7.blog.mappers;

import com.group7.blog.dto.Blog.request.BlogCreationRequest;
import com.group7.blog.dto.Blog.request.BlogUpdateRequest;
import com.group7.blog.dto.Blog.response.BlogDetailResponse;
import com.group7.blog.dto.Blog.response.BlogResponse;
import com.group7.blog.dto.Blog.response.UserWithBlogDetail;
import com.group7.blog.dto.BookMark.response.BookMarkResponse;
import com.group7.blog.dto.Tag.response.TagResponseBlogDetail;
import com.group7.blog.models.Blog;
import com.group7.blog.models.BlogTag;
import com.group7.blog.models.BookMark;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface BlogMapper {
    BlogMapper INSTANCE = Mappers.getMapper(BlogMapper.class);

    Blog toBlog(BlogCreationRequest request);

    @Mapping(target = "title", source = "title")
    @Mapping(target = "content", source = "content")
    @Mapping(target = "status", source = "status")
    @Mapping(target = "publishedAt", source = "publishedAt")
    void updateBlog(@MappingTarget Blog blog, BlogUpdateRequest request);

    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "updatedAt", source = "updatedAt")
    BlogResponse toBlogResponse(Blog blog);


    @Mapping(source = "users", target = "user")
    @Mapping(source = "category", target = "category")
    @Mapping(source = "blogTags", target = "tags", qualifiedByName = "mapTags")
    BlogDetailResponse toBlogDetailResponse(Blog blog);

    @Mapping(source = "blogTags", target = "tags", qualifiedByName = "mapTags")
    UserWithBlogDetail toUserWithBlogDetail(Blog blog);

    // Custom method to map BlogTags to TagResponseBlogDetail
    @Named("mapTags")
    public default List<TagResponseBlogDetail> mapTags(List<BlogTag> blogTags) {
        return Optional.ofNullable(blogTags)
                .orElse(Collections.emptyList())
                .stream()
                .map(blogTag -> new TagResponseBlogDetail(
                        blogTag.getTag().getId(),
                        blogTag.getTag().getName()
                ))
                .collect(Collectors.toList());
    }
}



