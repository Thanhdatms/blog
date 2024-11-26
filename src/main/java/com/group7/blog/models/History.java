package com.group7.blog.models;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class History {
    @Id
    @GeneratedValue
    Long id;
    String model;
    UUID objectId;
    String actionStatus;
    String actionType;
    String email;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    Users users;
}
