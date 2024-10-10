package com.group7.blog.services;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.group7.blog.dto.Blog.request.BlogCreationRequest;
import com.group7.blog.dto.Blog.request.BlogUpdateRequest;
import com.group7.blog.dto.Blog.response.BlogResponse;
import com.group7.blog.exceptions.AppException;
import com.group7.blog.enums.ErrorCode;
import com.group7.blog.mappers.BlogMapper;
import com.group7.blog.models.Blog;
import com.group7.blog.repositories.BlogRepository;
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
    BlogMapper blogMapper;
    CloudinaryService cloudinaryService;

    public BlogResponse createBlog(BlogCreationRequest request, MultipartFile file) {
        if (!file.isEmpty()) {
            request.setThumbnail(cloudinaryService.uploadFile(file, FOLDER_NAME));
        }
        Blog blog = blogMapper.toBlog(request);
        return blogMapper.toBlogResponse(blogRepository.save(blog));
    }

    public List<BlogResponse> getBlogs() {
        return blogRepository.findAll()
                             .stream()
                             .map(blogMapper::toBlogResponse)
                             .collect(Collectors.toList());
    }

    public BlogResponse getBlog(UUID blogId) {
        return blogMapper.toBlogResponse(
                blogRepository.findById(blogId)
                              .orElseThrow(() -> new AppException(ErrorCode.BLOG_NOT_EXISTED))
        );
    }

    public BlogResponse updateBlog(UUID blogId, BlogUpdateRequest request) {
        Blog blog = blogRepository.findById(blogId).orElseThrow(() -> new AppException(ErrorCode.BLOG_NOT_EXISTED));
        blogMapper.updateBlog(blog, request);
        return blogMapper.toBlogResponse(blogRepository.save(blog));
    }
}
