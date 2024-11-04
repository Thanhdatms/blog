package com.group7.blog.repositories;

import com.group7.blog.models.UserBlogVote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserBlogVoteRepository extends JpaRepository<UserBlogVote, UUID> {
}
