package com.group7.blog.services;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.group7.blog.dto.Blog.request.BlogCreationRequest;
import com.group7.blog.dto.Blog.request.BlogUpdateRequest;
import com.group7.blog.dto.Blog.response.BlogDetailResponse;
import com.group7.blog.dto.Blog.response.BlogResponse;
import com.group7.blog.dto.BlogTag.BlogTagCreation;
import com.group7.blog.dto.Tag.response.TagResponse;
import com.group7.blog.dto.Tag.response.TagResponseBlogDetail;
import com.group7.blog.exceptions.AppException;
import com.group7.blog.enums.ErrorCode;
import com.group7.blog.mappers.BlogMapper;
import com.group7.blog.mappers.BlogTagMapper;
import com.group7.blog.models.Blog;
import com.group7.blog.models.BlogTag;
import com.group7.blog.models.Tag;
import com.group7.blog.repositories.BlogRepository;
import com.group7.blog.repositories.BlogTagRepository;
import com.group7.blog.repositories.TagRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import static com.group7.blog.enums.Constant.FOLDER_NAME;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BlogService {
    BlogRepository blogRepository;
    TagRepository tagRepository;
    BlogTagRepository blogTagRepository;
    BlogMapper blogMapper;
    BlogTagMapper blogTagMapper;
    CloudinaryService cloudinaryService;

    public BlogResponse createBlog(BlogCreationRequest request, MultipartFile file) {
        if (file == null && file.isEmpty()) {
            request.setThumbnail(cloudinaryService.uploadFile(file, FOLDER_NAME));
        }
        Tag tag = tagRepository.findOneByName(request.getTagName());
        Blog blog = blogMapper.toBlog(request);
        blog = blogRepository.save(blog);
        BlogTag blogTag = blogTagMapper.toBlogTag(new BlogTagCreation(tag, blog));
        blogTagRepository.save(blogTag);
        return blogMapper.toBlogResponse(blog);
    }

    public List<BlogResponse> getBlogs() {
        return blogRepository.findAll()
                             .stream()
                             .map(blogMapper::toBlogResponse)
                             .collect(Collectors.toList());
    }

    public BlogDetailResponse getBlog(UUID blogId) {
        BlogDetailResponse blogDetailRes = blogMapper.toBlogDetailResponse(blogRepository.findById(blogId)
                              .orElseThrow(() -> new AppException(ErrorCode.BLOG_NOT_EXISTED)));
        List<TagResponseBlogDetail> tags = blogTagRepository.findAllTagsByBlogId(blogId);
        blogDetailRes.setTags(tags);
        return blogDetailRes;
    }

    public BlogResponse updateBlog(UUID blogId, BlogUpdateRequest request) {
        Blog blog = blogRepository.findById(blogId).orElseThrow(() -> new AppException(ErrorCode.BLOG_NOT_EXISTED));
        blogMapper.updateBlog(blog, request);
        return blogMapper.toBlogResponse(blogRepository.save(blog));
    }
}
