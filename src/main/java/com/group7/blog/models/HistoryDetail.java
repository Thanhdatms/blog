package com.group7.blog.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class HistoryDetail {
    @Id
    @GeneratedValue
    Long id;
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "history_id")
    History history;
    String fieldName;
    String newValue;
}
