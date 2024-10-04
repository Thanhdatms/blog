package com.group7.blog.models;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.sql.Timestamp;
import java.util.UUID;

@Entity
public class Users {

    @Id
    @GeneratedValue
    private UUID id;

    private String firstname;
    private String lastname;
    private String username;
    private String hashpassword;
    private String email;
    private String phonenumber;
    private String refeshtoken;
    private boolean status;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
