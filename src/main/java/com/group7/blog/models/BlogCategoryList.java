package com.group7.blog.models;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
public class BlogCategoryList {
    @Id
    @GeneratedValue
    private UUID id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoryId")
    private Category category;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "blogId")
    private Blog blog;




}
