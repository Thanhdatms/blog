package com.group7.blog.dto.Report.reponse;

import com.group7.blog.dto.User.reponse.UserInfoResponse;
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
}
