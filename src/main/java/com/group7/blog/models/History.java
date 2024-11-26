package com.group7.blog.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.util.List;
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
    @JsonIgnore
    Users users;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @CreationTimestamp
    Timestamp createdAt;

    @OneToMany(mappedBy = "history")
    @JsonIgnore
    List<HistoryDetail> historyDetails;

    public UUID getUsers() {
        return users.getId();
    }
}
