package com.group7.blog.dto.request;

import lombok.Getter;

import java.util.Date;
import java.util.UUID;

@Getter
public class UserCreationRequest {
    private UUID id;
    private String firstname;
    private String lastname;
    private String username;
    private String hashpassword;
    private String email;
    private String phonenumber;
    private String refeshtoken;
    private boolean status;
    private Date createdAt;
    private Date updatedAt;
}
