package com.group7.blog.models;

import jakarta.persistence.*;

import java.sql.Timestamp;
import java.util.Set;
import java.util.UUID;

@Entity
public class Category {
    @Id
    @GeneratedValue
    private UUID id;
    private String title;
    private String content;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private Timestamp publishedAt;
    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
    private Set<BlogCategoryList> blogCategoryLists;
}
