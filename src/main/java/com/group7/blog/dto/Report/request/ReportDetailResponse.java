package com.group7.blog.dto.Report.request;

import com.group7.blog.dto.Report.reponse.ReportResponse;
import com.group7.blog.dto.User.reponse.UserInfoResponse;
import com.group7.blog.enums.EnumData;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReportDetailResponse {

    String blogTitle;
    String thumbnail;
    ReportResponse reportResponse;
    UserInfoResponse user;
    EnumData.ReportType reportType;
}
