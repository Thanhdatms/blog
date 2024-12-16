package com.group7.blog.enums;

public class EnumData {

    public enum VoteType {
        UPVOTE,
        DOWNVOTE,
        NONE,
        ;
        public static VoteType fromString(String value) {
            try {
                return VoteType.valueOf(value);
            } catch (IllegalArgumentException e) {
                return NONE;
            }
        }
    }

    public enum BlogStatus {
        PUBLISHED,
        BANNED
    }

    public enum UserStatus {
        BANNED,
        ACTIVATED
    }

    public enum ReportType{
        USER,
        BLOG,
        OTHER
    }

    public enum ReportReason{
        SPAM,
        OFFENSIVE,
        INCORRECT,
        OTHER
    }

    public enum ReportStatus{
        PENDING,
        CANCEL,
        DELETE
    }


    public static ReportReason fromString(String value) {
        try {
            return ReportReason.valueOf(value);
        } catch (IllegalArgumentException e){
            return ReportReason.OTHER;
        }
    }
}
