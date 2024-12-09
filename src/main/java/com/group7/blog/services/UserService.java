package com.group7.blog.services;

import com.group7.blog.dto.Blog.response.BlogResponse;
import com.group7.blog.dto.BookMark.response.BookMarkListResponse;
import com.group7.blog.dto.User.reponse.UserProfileResponse;
import com.group7.blog.dto.User.reponse.UserResponse;
import com.group7.blog.dto.User.request.UserCreationRequest;
import com.group7.blog.dto.User.request.UserUpdateRequest;
import com.group7.blog.enums.ErrorCode;
import com.group7.blog.exceptions.AppException;
import com.group7.blog.mappers.BlogMapper;
import com.group7.blog.mappers.RoleMapper;
import com.group7.blog.mappers.UserMapper;
import com.group7.blog.models.*;
import com.group7.blog.repositories.*;
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
    UserFollowRepository userFollowRepository;
    RoleRepository roleRepository;
    UserRoleRepository userRoleRepository;
    RoleMapper roleMapper;


    public boolean checkUserExistById(String userId) {
        return userRepository.existsById(UUID.fromString(userId));
    }

    public List<Users> getUsers() {
        return userRepository.findAll();
    }

    public Users createUser(UserCreationRequest request){
        Users user = userMapper.toUser(request);
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        user.setHashPassword(passwordEncoder.encode(user.getHashPassword()));
        Role role = roleRepository.findByName("user").orElseThrow(()-> new AppException(ErrorCode.USER_ROLE_NOT_FOUND));
        user = userRepository.save(user);

        UserRole userRole = new UserRole();
        userRole.setUser(user);
        userRole.setRole(role);

        userRoleRepository.save(userRole);
        return user;
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

    public UserProfileResponse getCurrentUserInfo(String userId) {
        Users user = userRepository.findById(UUID.fromString(userId)).
                orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        return new UserProfileResponse(
                user.getId().toString(),
                user.getUsername(),
                user.getLastname(),
                user.getBio(),
                user.getAvatar(),
                user.getUserRoles().stream().map(
                        item -> roleMapper.toRoleResponse(item.getRole())
                ).toList()
        );
    }

    public UserResponse getBlogsByUserId(UUID userId) {
        Users user = userRepository.findUserWithBlogsById(userId);
        UserResponse userRes = userMapper.toUserResponse(user);
        // Load tags for each blog in a separate query if needed
       userRes.setBlogs(
               user.getBlogs()
               .stream().map(blogMapper::toBlogDetailResponse)
               .collect(Collectors.toList())
       );
       return userRes;
    }

    public String followUser(String targetUserId) {
        SecurityContext context = SecurityContextHolder.getContext();
        String sourceUserId = context.getAuthentication().getName();

        Users sourceUser = userRepository.findById(UUID.fromString(sourceUserId))
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        Users targetUser = userRepository.findById(UUID.fromString(targetUserId))
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        UserFollow follow = userFollowRepository.findByUserTargetSourceId(
                UUID.fromString(targetUserId), UUID.fromString(sourceUserId)
        );
        if(follow != null) {
            throw new AppException(ErrorCode.USER_FOLLOW_EXISTED);
        }

        UserFollow userFollow = new UserFollow();
        userFollow.setUserTargetId(targetUser);
        userFollow.setUserSourceId(UUID.fromString(sourceUserId));
        userFollowRepository.save(userFollow);
        return "Follow user successfully!";
    }

    public String unFollowUser(String targetUserId) {
        SecurityContext context = SecurityContextHolder.getContext();
        String sourceUserId = context.getAuthentication().getName();

        UserFollow follow = userFollowRepository.findByUserTargetSourceIdOptional(
                UUID.fromString(targetUserId), UUID.fromString(sourceUserId)
        ).orElseThrow(() -> new AppException(ErrorCode.USER_ID_INVALID));
        userFollowRepository.delete(follow);

        return "Unfollow user successfully";
    }

    public Boolean isFollowing(UUID targetUserId) {
        SecurityContext context = SecurityContextHolder.getContext();
        String sourceUserId = context.getAuthentication().getName();

        UserFollow follow = userFollowRepository.findByUserTargetSourceId(
                targetUserId, UUID.fromString(sourceUserId)
        );

        return follow != null;
    }
}
