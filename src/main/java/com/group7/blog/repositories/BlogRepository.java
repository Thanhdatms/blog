package com.group7.blog.repositories;

import com.group7.blog.dto.Blog.response.BlogDetailResponse;
import com.group7.blog.dto.Blog.response.BlogResponse;
import com.group7.blog.enums.EnumData;
import com.group7.blog.models.Blog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BlogRepository extends JpaRepository<Blog, UUID>, JpaSpecificationExecutor<Blog> {
    @Query("SELECT b FROM Blog b " +
            "JOIN FETCH b.users " +
            "JOIN FETCH b.blogTags")
    List<Blog> findAllBlogsWithUserAndTags();

    @Query("SELECT b FROM Blog b LEFT JOIN FETCH b.blogTags WHERE b.id = :blogId")
    Blog findBlogWithTagsById(@Param("blogId") UUID blogId);

    long countByUsersId(UUID userId);


    @Query("SELECT DISTINCT b FROM Blog b " +
            "LEFT JOIN b.category c " +
            "LEFT JOIN b.blogTags bt " +
            "LEFT JOIN bt.tag t " +
            "WHERE LOWER(b.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(b.content) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(c.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(t.name) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Blog> searchBlogs(String keyword, Pageable pageable);
}
