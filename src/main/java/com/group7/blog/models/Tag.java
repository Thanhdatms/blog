package com.group7.blog.models;


import jakarta.persistence.*;

import java.util.Set;
import java.util.UUID;

@Entity
public class Tag {
    @Id
    @GeneratedValue
    private UUID id;
    private String title;
    private String content;

    @OneToMany(mappedBy = "tag", fetch = FetchType.LAZY)
    private Set<BlogTag> blogTags;
}
