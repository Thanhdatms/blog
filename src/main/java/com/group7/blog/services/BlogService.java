package com.group7.blog.services;
import java.util.List;

import com.group7.blog.dto.request.BlogCreationRequest;
import com.group7.blog.dto.request.BlogUpdateRequest;
import com.group7.blog.exceptions.AppException;
import com.group7.blog.exceptions.ErrorCode;
import com.group7.blog.mappers.BlogMapper;
import com.group7.blog.models.Blog;
import com.group7.blog.repositories.BlogRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BlogService {
    BlogRepository blogRepository;
    BlogMapper blogMapper;

    public Blog createBlog(BlogCreationRequest request) {
        Blog blog = blogMapper.toBlog(request);
        return blogRepository.save(blog);
    }

    public List<Blog> getBlogs() {
        return blogRepository.findAll();
    }

    public Blog getBlog(String blogId) {
        return blogRepository
                .findById(blogId)
                .orElseThrow(() -> new AppException(ErrorCode.BLOG_NOT_EXISTED));
    }

    public  Blog updateBlog(String blogId, BlogUpdateRequest request) {
        Blog blog = blogRepository.findById(blogId).orElseThrow(() -> new AppException(ErrorCode.BLOG_NOT_EXISTED));
        blogMapper.updateBlog(blog, request);
        return blogRepository.save(blog);
    }
}
