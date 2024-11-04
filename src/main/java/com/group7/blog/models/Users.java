package com.group7.blog.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

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

    private String firstname;
    private String lastname;
    private String username;
    private String hashpassword;
    private String email;
    private String phonenumber;
    @Column(columnDefinition = "TEXT")
    private String refreshtoken;
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

    @OneToMany(mappedBy = "users", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Blog> blogs;
}
