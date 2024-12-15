package com.group7.blog.dto.Report.request;

import com.group7.blog.enums.EnumData;
import com.group7.blog.enums.EnumData.ReportType;
import com.group7.blog.models.Blog;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReportCreationRequest {
    UUID blogId;
    UUID userId;
    EnumData.ReportReason reportReason;
    ReportType reportType;
    String description;
}
