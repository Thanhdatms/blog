package com.group7.blog.dto.History.request;

import com.group7.blog.enums.EnumData;
import com.group7.blog.models.Users;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class HistoryCreation {
    String model;
    UUID objectId;
    EnumData.HistoryActionStatus actionStatus;
    EnumData.HistoryActionType actionType;
    String email;
    Users users;
}
