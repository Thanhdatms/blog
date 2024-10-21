package com.group7.blog.services;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.group7.blog.dto.Blog.response.BlogResponse;
import com.group7.blog.dto.Tag.request.TagCreateRequest;
import com.group7.blog.dto.Tag.request.TagUpdateRequest;
import com.group7.blog.dto.Tag.response.TagResponse;
import com.group7.blog.exceptions.AppException;
import com.group7.blog.enums.ErrorCode;
import com.group7.blog.mappers.TagMapper;
import com.group7.blog.models.Blog;
import com.group7.blog.models.Tag;
import com.group7.blog.repositories.BlogRepository;
import com.group7.blog.repositories.BlogTagRepository;
import com.group7.blog.repositories.TagRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TagService {
    TagRepository tagRepository;
    BlogTagRepository blogTagRepository;
    TagMapper tagMapper;

    public TagResponse createTag(TagCreateRequest request) {
        Tag tag = tagMapper.toTag(request);
        return tagMapper.toTagResponse(tagRepository.save(tag));
    }

    public List<TagResponse> getTags() {
        return tagRepository.findAll()
                            .stream()
                            .map(tagMapper::toTagResponse)
                            .collect(Collectors.toList());
    }

    public TagResponse getTag(UUID tagId) {
        return tagMapper.toTagResponse(
                tagRepository.findById(tagId).orElseThrow(() -> new AppException(ErrorCode.TAG_NOT_EXISTED))
        );
    }

    public TagResponse updateTag(UUID tagId, TagUpdateRequest request) {
        Tag tag = tagRepository.findById(tagId).orElseThrow(() -> new AppException(ErrorCode.TAG_NOT_EXISTED));
        tagMapper.updateTag(tag, request);
        return tagMapper.toTagResponse(tagRepository.save(tag));
    }

    public TagResponse getBlogsByTagId(UUID tagId) {
        Tag tag = tagRepository.findById(tagId).orElseThrow(() -> new AppException(ErrorCode.TAG_NOT_EXISTED));
        List<BlogResponse> blogs = blogTagRepository.findAllBlogsByTagId(tagId);
        TagResponse tagRes = tagMapper.toTagResponse(tag);
        tagRes.setBlogs(blogs);
        return tagRes;
    }
}