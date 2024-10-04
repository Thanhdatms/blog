package com.group7.blog.models;


import jakarta.persistence.*;

import java.util.Set;
import java.util.UUID;

@Entity
public class Vote {
    @Id
    @GeneratedValue
    private UUID id;
    private String title;
    @OneToMany(mappedBy = "vote", fetch = FetchType.LAZY)
    private Set<UserCommentVote> userCommentVotes;
    @OneToMany(mappedBy = "vote", fetch = FetchType.LAZY)
    private Set<UserBlogVote> userBlogVotes;


}
