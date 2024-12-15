package com.group7.blog.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {
    // GLOBAL ERRORS
    INTERNAL_SERVER_ERROR(500, "Internal server error", HttpStatus.INTERNAL_SERVER_ERROR),  // 500
    INVALID_KEY(400, "Uncategorized error", HttpStatus.BAD_REQUEST),  // 400

    // USER ERRORS
    USER_EXISTED(400, "User already exists", HttpStatus.BAD_REQUEST),  // 400
    USER_NOT_EXISTED(404, "User not found", HttpStatus.NOT_FOUND),  // 404
    UNAUTHENTICATED(401, "Unauthenticated", HttpStatus.UNAUTHORIZED),  // 401
    UNAUTHORIZED(403, "You do not have permission", HttpStatus.FORBIDDEN),  // 403
    INVALID_TOKEN(401, "Invalid token", HttpStatus.UNAUTHORIZED),  // 401
    INVALID_REFRESH_TOKEN(405, "Invalid refresh token", HttpStatus.UNAUTHORIZED),
    EMAIL_NOT_FOUND(404, "Email not found", HttpStatus.NOT_FOUND),  // 404
    EMAIL_REGISTERED(400, "Email is already registered", HttpStatus.BAD_REQUEST),  // 400
    RESET_PASSWD_EMAIL_SENT(400, "Reset Password email has been sent", HttpStatus.BAD_REQUEST),  // 200
    TOKEN_NOT_FOUND(404, "Token not found", HttpStatus.BAD_REQUEST),
    USER_ID_INVALID(400, "Invalid user ID", HttpStatus.BAD_REQUEST),  // 400
    USER_FOLLOW_EXISTED(409, "User already followed", HttpStatus.CONFLICT),  // 409 (409 is Conflict, better for "already followed" scenario)
    USER_ROLE_NOT_FOUND(404, "User role not found", HttpStatus.NOT_FOUND),
    // BLOG ERRORS
    BLOG_NOT_EXISTED(404, "Blog not found", HttpStatus.NOT_FOUND),  // 404
    FILE_MISSING(400, "Image file is missing", HttpStatus.BAD_REQUEST),  // 400

    PARENT_COMMENT_NOT_EXISTED(404, "Parent comment not found", HttpStatus.NOT_FOUND),  // 404

    // TAG ERRORS
    TAG_NOT_EXISTED(404, "Tag not found", HttpStatus.NOT_FOUND),  // 404
    TAG_EXISTED(400, "Tag already exists", HttpStatus.BAD_REQUEST),  // 400

    // CATEGORY ERRORS
    CATEGORY_NOT_EXISTED(404, "Category not found", HttpStatus.NOT_FOUND),  // 404
    CATEGORY_EXISTED(400, "Category already exists", HttpStatus.BAD_REQUEST),  // 400

    // VOTE ERRORS
    VOTETYPE_NOT_EXISTED(400, "Invalid vote type, must be UPVOTE or DOWNVOTE", HttpStatus.BAD_REQUEST),  // 400
    VOTE_EXISTED(409, "Vote already exists", HttpStatus.CONFLICT),  // 409 (Conflict: Already voted)
    VOTE_NOT_FOUND(404, "Vote not found", HttpStatus.NOT_FOUND),  // 404

    // BOOKMARK ERRORS
    BOOKMARK_NOT_FOUND(404, "Bookmark not found", HttpStatus.NOT_FOUND),  // 404
    BOOKMARK_EXISTED(409, "Bookmark already exists", HttpStatus.CONFLICT),  // 409

    // COMMENT ERRORS
    COMMENT_NOT_FOUND(404, "Comment not found", HttpStatus.NOT_FOUND),  // 404
    COMMENT_NOT_BELONG_TO_BLOG(400, "Comment does not belong to this blog", HttpStatus.BAD_REQUEST),  // 400
    COMMENT_DELETE_PERMISSION_ERROR(403, "User does not have permission to delete this comment", HttpStatus.FORBIDDEN),  // 403

    // REPORT ERRORS
    REPORT_NOT_EXIST(404, "Report not found", HttpStatus.NOT_FOUND);  // 404
    ;
    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;
}
