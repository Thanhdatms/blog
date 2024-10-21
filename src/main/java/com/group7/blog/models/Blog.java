package com.group7.blog.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.util.List;
import java.util.Set;
import java.util.UUID;


@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@JsonIgnoreProperties({"createdAt", "updatedAt"})
public class Blog {
    @Id
    @GeneratedValue
    UUID id;
    String title;
    String content;
    String summary;
    String thumbnail;
    boolean status;
    @CreationTimestamp
    Timestamp createdAt;
    @UpdateTimestamp
    Timestamp updatedAt;
    Timestamp publishedAt;
    @OneToMany(mappedBy = "blog", fetch = FetchType.LAZY)
    Set<BlogCategoryList> blogCategoryLists;
    @OneToMany(mappedBy = "blog")
    List<BlogTag> blogTags;
    @OneToMany(mappedBy = "blog", fetch = FetchType.LAZY)
    Set<BlogRegistration> blogRegistrations;
    @OneToMany(mappedBy = "blog", fetch = FetchType.LAZY)
    Set<Comment> comments;
    @OneToMany(mappedBy = "blog", fetch = FetchType.LAZY)
    Set<UserBlogVote> userBlogVotes;
}
