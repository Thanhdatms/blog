package com.group7.blog.models;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.sql.Timestamp;
import java.util.Set;

import static org.hibernate.annotations.UuidGenerator.Style.TIME;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Blog {
    @Id
    @GeneratedValue
    @UuidGenerator(style = TIME)
    String id;
    String title;
    String content;
    String summary;
    boolean status;
    @CreationTimestamp
    Timestamp createdAt;
    @UpdateTimestamp
    Timestamp updatedAt;
    Timestamp publishedAt;
    @OneToMany(mappedBy = "blog", fetch = FetchType.LAZY)
    Set<BlogCategoryList> blogCategoryLists;
    @OneToMany(mappedBy = "blog", fetch = FetchType.LAZY)
    Set<BlogTag> blogTags;
    @OneToMany(mappedBy = "blog", fetch = FetchType.LAZY)
    Set<BlogRegistration> blogRegistrations;
    @OneToMany(mappedBy = "blog", fetch = FetchType.LAZY)
    Set<Comment> comments;
    @OneToMany(mappedBy = "blog", fetch = FetchType.LAZY)
    Set<UserBlogVote> userBlogVotes;
}
