package com.group7.blog.services;

import com.group7.blog.dto.request.UserCreationRequest;
import com.group7.blog.mapper.UserMapper;
import com.group7.blog.models.Users;
import com.group7.blog.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserMapper userMapper;

    public List<Users> getUsers() {
        return userRepository.findAll();
    }

    public Users createUser(UserCreationRequest request){
        Users user = userMapper.toUser(request);

        return userRepository.save(user);
    }

    public Users getUser(UUID userId){
        return userRepository.findById(userId).orElseThrow(() -> new RuntimeException("The user is not exist"));
    }


}
