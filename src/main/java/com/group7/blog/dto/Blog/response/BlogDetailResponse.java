package com.group7.blog.dto.Blog.response;

import com.group7.blog.dto.Tag.response.TagResponse;
import com.group7.blog.dto.Tag.response.TagResponseBlogDetail;
import com.group7.blog.models.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BlogDetailResponse {
    UUID id;
    String title;
    String content;
    String summary;
    String thumbnail;
    boolean status;
    Timestamp publishedAt;
    Set<BlogCategoryList> blogCategoryLists;
    List<TagResponseBlogDetail> tags;
    Set<BlogRegistration> blogRegistrations;
    Set<Comment> comments;
    Set<UserBlogVote> userBlogVotes;
}
