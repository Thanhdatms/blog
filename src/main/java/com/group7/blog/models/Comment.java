package com.group7.blog.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Data
public class Comment {

    @Id
    @GeneratedValue
    private UUID id;

    // Many comments belong to one blog
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "blog_id", nullable = false)
    private Blog blog;

    // Many comments are written by one user
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Users users;

    // Many comments can have one parent comment (self-referencing)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id", nullable = true)
    private Comment parentComment;

    // One comment can have many child comments (cascade delete for children)
    @OneToMany(mappedBy = "parentComment",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    @JsonIgnore
    private List<Comment> comments;

    // One comment can have many user votes
    @OneToMany(mappedBy = "comment", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<UserCommentVote> userCommentVotes;

    private String content;

    private boolean isUpdate = false;;

    // Automatically set the creation timestamp
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @CreationTimestamp
    private Timestamp createdAt;

    // Automatically set the last updated timestamp
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @UpdateTimestamp
    private Timestamp updatedAt;
}
