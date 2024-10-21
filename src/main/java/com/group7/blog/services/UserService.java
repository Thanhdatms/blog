package com.group7.blog.services;

import com.group7.blog.dto.User.reponse.UserResponse;
import com.group7.blog.dto.User.request.UserCreationRequest;
import com.group7.blog.dto.User.request.UserUpdateRequest;
import com.group7.blog.mappers.UserMapper;
import com.group7.blog.models.Users;
import com.group7.blog.repositories.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class UserService {
    UserRepository userRepository;
    UserMapper userMapper;

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

}
