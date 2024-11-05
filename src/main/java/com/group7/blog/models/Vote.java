package com.group7.blog.models;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.Set;
import java.util.UUID;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Vote {
    @Id
    @GeneratedValue
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(unique = true, nullable = false)
    private VoteType voteType;

    @OneToMany(mappedBy = "vote", fetch = FetchType.LAZY)
    private Set<UserCommentVote> userCommentVotes;

    @OneToMany(mappedBy = "vote", fetch = FetchType.LAZY)
    private Set<UserBlogVote> userBlogVotes;

    public enum VoteType {
        UPVOTE,
        DOWNVOTE
    }

}


