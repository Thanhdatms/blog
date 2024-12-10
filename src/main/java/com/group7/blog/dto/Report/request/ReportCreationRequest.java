package com.group7.blog.dto.Report.request;

import com.group7.blog.enums.EnumData.ReportType;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReportCreationRequest {

    ReportType reportType;
    String description;


}
