package com.group7.blog.dto.Report.reponse;


import com.group7.blog.enums.EnumData;
import com.group7.blog.models.Blog;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.sql.Timestamp;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReportResponse {
    UUID blogId;
    EnumData.ReportType reportType;
    String description;
    Timestamp createdAt;
}
