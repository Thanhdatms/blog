package com.group7.blog.repositories;

import com.group7.blog.models.Users;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<Users, UUID> {
    boolean existsByUsername(String username);
    Optional<Users> findByUsername(String username);

    @Query("SELECT u FROM Users u LEFT JOIN FETCH u.blogs WHERE u.id = :userId")
    Users findUserWithBlogsById(@Param("userId") UUID userId);

    Optional<Users> findByEmail(String email);
    Users findOneByEmail(String email);

    Optional<Users> findByNameTag(String nameTag);

    @Query("SELECT u AS user, COUNT(b.id) AS blogCount " +
            "FROM Users u JOIN u.blogs b " +
            "GROUP BY u " +
            "ORDER BY blogCount DESC")
    List<Object[]> findTopUsersByBlogCount(Pageable pageable);
}
