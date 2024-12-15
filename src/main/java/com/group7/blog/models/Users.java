package com.group7.blog.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.group7.blog.enums.EnumData;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Data
public class Users {

    @Id
    @GeneratedValue
    private UUID id;

    @NotBlank(message = "Username cannot be blank")
    @Column(unique = true)
    private String username;

    @NotBlank(message = "NameTag cannot be blank")
    @Column(unique = true)
    private String nameTag;

    @NotBlank(message = "Password cannot be blank")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).*$",
            message = "Password must contain at least one digit, one lowercase, one uppercase letter, and one special character")
    private String hashPassword;

    @Column(columnDefinition = "TEXT")
    private String avatar;

    @Column(columnDefinition = "TEXT")
    private String bio;

    @Column(unique = true)
    @Email(message = "Email should be valid")
    @NotBlank(message = "Email cannot be blank")
    private String email;

    private String phoneNumber;
    @Column(columnDefinition = "TEXT")
    private String refreshToken;

    private boolean status;

    @Enumerated(EnumType.STRING)
    EnumData.UserStatus userStatus = EnumData.UserStatus.ACTIVATED;

    private Timestamp createdAt;
    private Timestamp updatedAt;

    // Automatically set createdAt when the entity is created
    @PrePersist
    protected void onCreate() {
        createdAt = Timestamp.from(Instant.now());
        updatedAt = Timestamp.from(Instant.now());
    }

    // Automatically update updatedAt when the entity is updated
    @PreUpdate
    protected void onUpdate() {
        updatedAt = Timestamp.from(Instant.now());
    }

    @JsonIgnore
    @OneToMany(mappedBy = "users", fetch = FetchType.LAZY)
    private Set<UserCommentVote> userCommentVotes;

    @JsonIgnore
    @OneToMany(mappedBy = "users", fetch = FetchType.LAZY)
    private Set<Comment> comments;

    @OneToMany(mappedBy = "users", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Blog> blogs;

    @OneToMany(mappedBy = "user")
    private List<BookMark> bookMarks;

    @OneToMany(mappedBy = "user")
    private List<UserRole> userRoles;

    @OneToMany(mappedBy = "users", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Report> reports;

}
