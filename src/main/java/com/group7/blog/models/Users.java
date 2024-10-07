package com.group7.blog.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
public class Users {

    @Id
    @GeneratedValue
    private UUID id;

    private String firstname;
    private String lastname;
    private String username;
    private String hashpassword;
    private String email;
    private String phonenumber;
    private String refeshtoken;
    private boolean status;
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
    private Set<UserBlogVote> userBlogVotes;

    @JsonIgnore
    @OneToMany(mappedBy = "users", fetch = FetchType.LAZY)
    private Set<UserCommentVote> userCommentVotes;

    @JsonIgnore
    @OneToMany(mappedBy = "users", fetch = FetchType.LAZY)
    private Set<Comment> comments;

    @JsonIgnore
    @OneToMany(mappedBy = "users", fetch = FetchType.LAZY)
    private Set<BlogRegistration> blogRegistrations;
}
