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
}
