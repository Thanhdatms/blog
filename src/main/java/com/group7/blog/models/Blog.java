package com.group7.blog.models;

import jakarta.persistence.*;

import java.sql.Timestamp;
import java.util.Set;
import java.util.UUID;

@Entity
public class Blog {
    @Id
    @GeneratedValue
    private UUID id;
    private String title;
    private String content;
    private String summary;
    private boolean status;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private Timestamp publishedAt;
    @OneToMany(mappedBy = "blog", fetch = FetchType.LAZY)
    private Set<BlogCategoryList> blogCategoryLists;
    @OneToMany(mappedBy = "blog", fetch = FetchType.LAZY)
    private Set<BlogTag> blogTags;
    @OneToMany(mappedBy = "blog", fetch = FetchType.LAZY)
    private Set<BlogRegistration> blogRegistrations;
    @OneToMany(mappedBy = "blog", fetch = FetchType.LAZY)
    private Set<Comment> comments;
    @OneToMany(mappedBy = "blog", fetch = FetchType.LAZY)
    private Set<UserBlogVote> userBlogVotes;

}
