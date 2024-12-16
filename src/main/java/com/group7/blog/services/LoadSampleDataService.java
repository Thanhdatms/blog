package com.group7.blog.services;

import com.group7.blog.enums.ErrorCode;
import com.group7.blog.exceptions.AppException;
import com.group7.blog.models.Role;
import com.group7.blog.models.UserRole;
import com.group7.blog.models.Users;
import com.group7.blog.repositories.RoleRepository;
import com.group7.blog.repositories.UserRepository;
import com.group7.blog.repositories.UserRoleRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LoadSampleDataService {
    UserRoleRepository userRoleRepository;
    RoleRepository roleRepository;
    UserRepository userRepository;

    public void loadUserData() {
        try{
            Role roleAdmin = new Role("admin");
            Role roleUser = new Role("user");
            roleAdmin = roleRepository.save(roleAdmin);
            roleUser = roleRepository.save(roleUser);
            System.out.println("Create user role!");

            Users admin = new Users();
            admin.setEmail("adminMEB@gmail.com");
            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
            admin.setHashPassword(passwordEncoder.encode("admin123456"));
            admin.setUsername("adminMEB");

            userRepository.save(admin);

            UserRole adminRole = new UserRole();
            adminRole.setRole(roleAdmin);
            adminRole.setUser(admin);
            userRoleRepository.save(adminRole);
        } catch (RuntimeException e) {
            throw new AppException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }
}
