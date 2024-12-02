package com.group7.blog.models;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.sql.Timestamp;
import java.util.List;
import java.util.Set;
import java.util.UUID;
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Category {
    @Id
    @GeneratedValue
    UUID id;
    @Column(unique = true)
    String title;
    String content;
    Timestamp createdAt;
    Timestamp updatedAt;
    Timestamp publishedAt;

    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST,  orphanRemoval = false)
    List<Blog> blogs;
}
