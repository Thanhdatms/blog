package com.group7.blog.models;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class BlogTag {
    @Id
    @GeneratedValue
    Integer id;

    @ManyToOne
    @JoinColumn(name = "blog_id")
    Blog blog;

    @ManyToOne
    @JoinColumn(name = "tag_id")
    Tag tag;

    public UUID getBlog() {
        return blog.getId();
    }

    public UUID getTag() {
        return tag.getId();
    }
}
