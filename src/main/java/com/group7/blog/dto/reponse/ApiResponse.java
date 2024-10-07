package com.group7.blog.dto.reponse;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Entity;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse <T> {
    private int code;
    private String message;
    private T metadata;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getMetadata() {
        return metadata;
    }

    public void setMetadata(T metadata) {
        this.metadata = metadata;
    }
}
