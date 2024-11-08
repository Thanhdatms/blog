package com.group7.blog.services;

import com.group7.blog.dto.Blog.response.BlogResponse;
import com.group7.blog.dto.User.reponse.UserProfileResponse;
import com.group7.blog.dto.User.reponse.UserResponse;
import com.group7.blog.dto.User.request.UserCreationRequest;
import com.group7.blog.dto.User.request.UserUpdateRequest;
import com.group7.blog.enums.ErrorCode;
import com.group7.blog.exceptions.AppException;
import com.group7.blog.mappers.BlogMapper;
import com.group7.blog.mappers.UserMapper;
import com.group7.blog.models.Users;
import com.group7.blog.repositories.BlogRepository;
import com.group7.blog.repositories.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
@Slf4j
public class UserService {
    UserRepository userRepository;
    UserMapper userMapper;
    BlogRepository blogRepository;
    BlogMapper blogMapper;

    public boolean checkUserExistById(String userId) {
        return userRepository.existsById(UUID.fromString(userId));
    }

    public List<Users> getUsers() {
        return userRepository.findAll();
    }

    public Users createUser(UserCreationRequest request){
        Users user = userMapper.toUser(request);
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        user.setHashpassword(passwordEncoder.encode(user.getHashpassword()));
        return userRepository.save(user);
    }

    public Users getUser(UUID userId){
        return userRepository.findById(userId).orElseThrow(() -> new RuntimeException("The user is not exist"));
    }

    public UserResponse updateUser(UUID userId, UserUpdateRequest request){
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        userMapper.updateUser(user, request);
        System.out.println(userMapper);
        return userMapper.toUserResponse(userRepository.save(user));
    }

    public UserProfileResponse getCurrentUserInfo() {
        SecurityContext context = SecurityContextHolder.getContext();
        String userId = context.getAuthentication().getName();

        Users user = userRepository.findById(UUID.fromString(userId)).
                orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        return userMapper.toUserProfileResponse(user);
    }

    public UserResponse getBlogsByUserId(UUID userId) {
        Users user = userRepository.findUserWithBlogsById(userId);
        UserResponse userRes = userMapper.toUserResponse(user);
        // Load tags for each blog in a separate query if needed
       userRes.setBlogs(
               user.getBlogs()
               .stream().map(blogMapper::toUserWithBlogDetail)
               .collect(Collectors.toList())
       );
       return userRes;
    }
}
