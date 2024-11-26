package com.group7.blog.services;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.group7.blog.dto.Blog.request.BlogCreationRequest;
import com.group7.blog.dto.Blog.request.BlogUpdateRequest;
import com.group7.blog.dto.Blog.response.BlogDetailResponse;
import com.group7.blog.dto.Blog.response.BlogResponse;
import com.group7.blog.dto.BlogTag.BlogTagCreation;
import com.group7.blog.dto.History.request.HistoryCreation;
import com.group7.blog.dto.History.request.HistoryDetailCreation;
import com.group7.blog.enums.EnumData;
import com.group7.blog.exceptions.AppException;
import com.group7.blog.enums.ErrorCode;
import com.group7.blog.mappers.BlogMapper;
import com.group7.blog.mappers.BlogTagMapper;
import com.group7.blog.mappers.UserMapper;
import com.group7.blog.models.*;
import com.group7.blog.repositories.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
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
    UserRepository userRepository;
    UserMapper userMapper;
    CategoryRepository categoryRepository;
    HistoryService historyService;

    public BlogDetailResponse createBlog(BlogCreationRequest request, MultipartFile file) {
        SecurityContext context = SecurityContextHolder.getContext();
        String userId = context.getAuthentication().getName();

        Users user = userRepository
                .findById(UUID
                .fromString(userId))
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        List<Tag> tags = request.getTags()
                                .stream()
                                .map(
                                        tagName -> tagRepository
                                                    .findOneByName(tagName)
                                                    .orElseThrow(() -> new AppException(ErrorCode.TAG_NOT_EXISTED))
                                )
                                .toList();
        Category category = categoryRepository
                .findOneByTitle(request.getCategoryName())
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_EXISTED));

        if (file != null && !file.isEmpty()) {
            request.setThumbnail(cloudinaryService.uploadFile(file, FOLDER_NAME));
        }
        Blog blog = blogMapper.toBlog(request);
        blog.setUsers(user);
        blog.setCategory(category);
        blog = blogRepository.save(blog);
        Blog finalBlog = blog;
        tags.forEach(tag ->
                blogTagRepository.save(
                        blogTagMapper.toBlogTag(new BlogTagCreation(tag, finalBlog))
                )
        );

        HistoryCreation historyCreation = new HistoryCreation();
        historyCreation.setEmail(user.getEmail());
        historyCreation.setUsers(user);
        historyCreation.setObjectId(blog.getId());
        historyCreation.setModel(Blog.class.getSimpleName());
        historyCreation.setActionType(EnumData.HistoryActionType.CREATE);
        historyCreation.setActionStatus(EnumData.HistoryActionStatus.SUCCESSFUL);
        historyService.createHistory(historyCreation);

        return blogMapper.toBlogDetailResponse(blog);
    }

    public List<BlogDetailResponse> getBlogs() {
        return blogRepository
                .findAll()
                .stream()
                .map(blogMapper::toBlogDetailResponse)
                .collect(Collectors.toList());
    }

    public BlogDetailResponse getBlog(UUID blogId) {
        Blog blog = blogRepository
                .findById(blogId)
                .orElseThrow(() -> new AppException(ErrorCode.BLOG_NOT_EXISTED));
        return blogMapper.toBlogDetailResponse(blog);
    }

    public BlogResponse updateBlog(UUID blogId, BlogUpdateRequest request,  MultipartFile file) {
        SecurityContext context = SecurityContextHolder.getContext();
        String userId = context.getAuthentication().getName();

        Blog blog = blogRepository.findById(blogId).orElseThrow(() -> new AppException(ErrorCode.BLOG_NOT_EXISTED));

        if (file != null && !file.isEmpty()) {
            request.setThumbnail(cloudinaryService.uploadFile(file, FOLDER_NAME));
        }

        if(request.getTags() != null && !request.getTags().isEmpty()) {
            List<Tag> tags = request.getTags()
                    .stream()
                    .map(
                            tagName -> tagRepository
                                    .findOneByName(tagName)
                                    .orElseThrow(() -> new AppException(ErrorCode.TAG_NOT_EXISTED))
                    )

                    .toList();
            blogTagRepository.findAllByBlogId(blogId).forEach(blogTagRepository::delete);
            tags.forEach(tag ->
                    blogTagRepository.save(
                            blogTagMapper.toBlogTag(new BlogTagCreation(tag, blog))
                    )
            );
        }

        if(request.getCategoryName() != null && !request.getCategoryName().isEmpty()) {
            Category category = categoryRepository
                    .findOneByTitle(request.getCategoryName())
                    .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_EXISTED));

            blog.setCategory(category);
        }

        blogMapper.updateBlog(blog, request);

        Users user = userRepository
                .findById(UUID
                        .fromString(userId))
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        HistoryCreation historyCreation = new HistoryCreation();
        historyCreation.setEmail(user.getEmail());
        historyCreation.setUsers(user);
        historyCreation.setObjectId(blog.getId());
        historyCreation.setModel(Blog.class.getSimpleName());
        historyCreation.setActionType(EnumData.HistoryActionType.UPDATE);
        historyCreation.setActionStatus(EnumData.HistoryActionStatus.SUCCESSFUL);
        History history = historyService.createHistory(historyCreation);
        List<HistoryDetailCreation> list = historyService.getNonNullFieldNames(request, history);
        historyService.createHistoryDetails(list);

        return blogMapper.toBlogResponse(blogRepository.save(blog));
    }
}
