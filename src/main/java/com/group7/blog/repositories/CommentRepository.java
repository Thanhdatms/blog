package com.group7.blog.repositories;

import com.group7.blog.models.Blog;
import com.group7.blog.models.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CommentRepository extends JpaRepository<Comment, UUID> {
    Page<Comment> findByBlogAndParentCommentIsNull(Blog blog, Pageable pageable);
}
