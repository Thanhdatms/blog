package com.group7.blog.services;

import com.group7.blog.dto.Auth.TokenCreation;
import com.group7.blog.dto.User.reponse.*;
import com.group7.blog.dto.User.request.ResetPasswordDTO;
import com.group7.blog.dto.User.request.UpdateProfileRequestDTO;
import com.group7.blog.dto.User.request.UserCreationRequest;
import com.group7.blog.dto.User.request.UserUpdateRequest;
import com.group7.blog.enums.ErrorCode;
import com.group7.blog.exceptions.AppException;
import com.group7.blog.mappers.BlogMapper;
import com.group7.blog.mappers.UserMapper;
import com.group7.blog.models.PasswordResetToken;
import com.group7.blog.models.UserFollow;
import com.group7.blog.models.Users;
import com.group7.blog.repositories.BlogRepository;
import com.group7.blog.repositories.PasswordResetTokenRepository;
import com.group7.blog.repositories.UserFollowRepository;
import com.group7.blog.repositories.UserRepository;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.BadJwtException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

import static com.group7.blog.enums.Constant.FOLDER_NAME;
import static com.group7.blog.enums.Constant.resetPasswordUrl;


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
    EmailService emailService;
    TokenService tokenService;
    PasswordResetTokenRepository passwordResetTokenRepository;
    CloudinaryService cloudinaryService;

    public boolean checkUserExistById(String userId) {
        return userRepository.existsById(UUID.fromString(userId));
    }

    public List<Users> getUsers() {
        return userRepository.findAll();
    }

    public UserResponse createUser(UserCreationRequest request, MultipartFile file){
        if (userRepository.findByEmail(request.getEmail()).isPresent()) throw new AppException(ErrorCode.EMAIL_REGISTERED);

        if (file != null && !file.isEmpty()) {
            request.setAvatar(cloudinaryService.uploadFile(file, FOLDER_NAME));
        }

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        Users user = userMapper.toUser(request);
        user.setHashPassword(passwordEncoder.encode(request.getPassword()));

        return userMapper.toUserResponse(userRepository.save(user));
    }

    public UserProfileResponseDTO getUserById(UUID userId){
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("The user is not exist"));

        return userMapper.toUserProfileResponse(user);
    }

    public UserProfileResponseDTO getUserByNameTag(String nameTag){
        Users user = userRepository.findByNameTag(nameTag)
                .orElseThrow(() -> new RuntimeException("User is not exist"));

        return userMapper.toUserProfileResponse(user);
    }

    public UserResponse updateUser(UUID userId, UserUpdateRequest request){
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        userMapper.updateUser(user, request);
        System.out.println(userMapper);
        return userMapper.toUserResponse(userRepository.save(user));
    }

    public UserProfileResponseDTO getCurrentUserInfo() {
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

    public String resetPassword(ResetPasswordDTO request) {
        Users user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new AppException(ErrorCode.EMAIL_NOT_FOUND));

        List<PasswordResetToken> list = passwordResetTokenRepository.findByUserIdAndValid(
                user.getId(), true
        );

        list.forEach(item -> {
            item.setValid(false);
            passwordResetTokenRepository.save(item);
        });

        String resetPasswordToken = tokenService.generateResetPasswordToken(
                new TokenCreation(user.getId(), user.getUsername())
        );
        String url = resetPasswordUrl + resetPasswordToken;

        Map<String, Object> model = new HashMap<>();
        model.put("username", user.getUsername());
        model.put("resetLink", url);

        emailService.sendEmail(
                user.getEmail(),
                "Reset Password",
                model,
                "reset-password-email-template"
        );

        PasswordResetToken passwordResetToken = new PasswordResetToken();
        passwordResetToken.setToken(resetPasswordToken);
        passwordResetToken.setUser(user);

        passwordResetTokenRepository.save(passwordResetToken);

        return "Request reset password successfully!";
    }

    public String verifyResetPasswordToken(String token) {
        try {

            PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByToken(token)
                    .orElseThrow(() -> new AppException(ErrorCode.INVALID_TOKEN));
            if(!passwordResetToken.isValid())
                throw new AppException(ErrorCode.INVALID_TOKEN);

            SignedJWT result = tokenService.verifyToken(token, false);
            passwordResetToken.setValid(false);
            passwordResetTokenRepository.save(passwordResetToken);
            return "Valid token!";
        } catch (AppException | JOSEException | ParseException e) {
            throw new BadJwtException("Invalid token!");
        }
    }

    public String changePassword(ChangePasswordDTO request) {
        SecurityContext context = SecurityContextHolder.getContext();
        String userId = context.getAuthentication().getName();

        Users user = userRepository.findById(UUID.fromString(userId)).
                orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        boolean isMatched = passwordEncoder.matches(request.getOldPassword(), user.getHashPassword());
        if(!isMatched){
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        user.setHashPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
        return "Change Password Successfully!";
    }

    public UserProfileResponseDTO updateProfile(
            UpdateProfileRequestDTO request,
            MultipartFile file
    ) {
        SecurityContext context = SecurityContextHolder.getContext();
        String userId = context.getAuthentication().getName();

        Users user = userRepository.findById(UUID.fromString(userId)).
                orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        if (file != null && !file.isEmpty()) {
            request.setAvatar(cloudinaryService.uploadFile(file, FOLDER_NAME));
        }

        userMapper.updateUserProfile(user, request);
        user.setAvatar(request.getAvatar());
        userRepository.save(user);

        return userMapper.toUserProfileResponse(user);
    }

    public UserStatsResponseDTO getUserStats(UUID userId) {
        UserStatsResponseDTO userStatsResponseDTO = new UserStatsResponseDTO();
        userStatsResponseDTO.setId(userId);
        userStatsResponseDTO.setFollowing(userFollowRepository.countByUserSourceId(userId));
        userStatsResponseDTO.setFollowers(userFollowRepository.countByUserTargetIdId(userId));
        userStatsResponseDTO.setPosts(blogRepository.countByUsersId(userId));
        userStatsResponseDTO.setTotalUpvote(userRepository.totalUpvotes(userId));
        userStatsResponseDTO.setTotalDownVote(userStatsResponseDTO.getTotalDownVote());
        return userStatsResponseDTO;
    }

    public List<TopUserResponse> getTopUsers() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Object[]> results = userRepository.findTopUsersRaw(pageable);

        return results.stream()
                .map(result -> {
                    Users user = (Users) result[0];
                    long blogCount = (long) result[1];
                    long totalUpvotes = ((Number) result[2]).longValue();

                    double score = 0.6 * blogCount + 0.4 * totalUpvotes;

                    UserResponse userResponse = userMapper.toUserResponse(user);

                    return TopUserResponse.builder()
                            .userResponse(userResponse)
                            .score(score)
                            .build();
                })
                .sorted(Comparator.comparingDouble(TopUserResponse::getScore).reversed())
                .toList();
    }


}
