package com.group7.blog.services;

import com.group7.blog.dto.BookMark.response.BookMarkListResponse;
import com.group7.blog.dto.BookMark.response.BookMarkResponse;
import com.group7.blog.enums.ErrorCode;
import com.group7.blog.exceptions.AppException;
import com.group7.blog.mappers.BookMarkMapper;
import com.group7.blog.models.Blog;
import com.group7.blog.models.BookMark;
import com.group7.blog.models.Users;
import com.group7.blog.repositories.BlogRepository;
import com.group7.blog.repositories.BookMarkRepository;
import com.group7.blog.repositories.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookMarkService {

    BlogRepository blogRepository;
    BookMarkRepository bookMarkRepository;
    UserRepository userRepository;
    BookMarkMapper bookMarkMapper;

    public BookMarkResponse saveBlog(UUID blogId) {
        SecurityContext context = SecurityContextHolder.getContext();
        String userId = context.getAuthentication().getName();

        Users user = userRepository.findById(UUID.fromString(userId))
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        Blog blog = blogRepository.findById(blogId)
                .orElseThrow(() -> new AppException(ErrorCode.BLOG_NOT_EXISTED));

        BookMark bookMark = new BookMark();
        bookMark.setBlog(blog);
        bookMark.setUser(user);

        return bookMarkMapper.toBookMarkResponse(bookMarkRepository.save(bookMark));
    }

    public String deleteBlog(UUID blogId) {
        SecurityContext context = SecurityContextHolder.getContext();
        String userId = context.getAuthentication().getName();
        BookMark bookMark = bookMarkRepository
                .findByUserBlogIdOptional(UUID.fromString(userId), blogId)
                .orElseThrow(() -> new AppException(ErrorCode.BOOKMARK_NOT_EXISTED));
        bookMarkRepository.delete(bookMark);
        return "Remove Blog Successfully!";
    }

}
