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

    public enum ReportType{
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


    public static ReportType fromString(String value) {
        try {
            return ReportType.valueOf(value);
        } catch (IllegalArgumentException e){
            return ReportType.OTHER;
        }
    }
}
