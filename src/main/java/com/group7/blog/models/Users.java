package com.group7.blog.models;


import jakarta.persistence.*;

import java.sql.Timestamp;
import java.util.Set;
import java.util.UUID;

@Entity
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

    @OneToMany(mappedBy = "users", fetch = FetchType.LAZY)
    private Set<UserBlogVote> userBlogVotes;
    @OneToMany(mappedBy = "users", fetch = FetchType.LAZY)
    private Set<UserCommentVote> userCommentVotes;
    @OneToMany(mappedBy = "users", fetch = FetchType.LAZY)
    private Set<Comment> comments;
    @OneToMany(mappedBy = "users", fetch = FetchType.LAZY)
    private Set<BlogRegistration> blogRegistrations;

}
