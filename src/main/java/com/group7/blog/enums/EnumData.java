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
                return NONE;  // Default value if invalid
            }
        }
    }

    public enum HistoryActionType {
        LOGIN,
        CREATE,
        UPDATE,
        DELETE;

        public static HistoryActionType fromString(String value) {
            return HistoryActionType.valueOf(value);
        }
    }

    public enum HistoryActionStatus {
        SUCCESSFUL,
        FAILED;

        public static HistoryActionStatus fromString(String value) {
            return HistoryActionStatus.valueOf(value);
        }
    }

    public enum BlogStatus {
        PUBLISHED,
        BANNED
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
        APPROVE
    }


    public static ReportType fromString(String value) {
        try {
            return ReportType.valueOf(value);
        } catch (IllegalArgumentException e){
            return ReportType.OTHER;
        }
    }
}
