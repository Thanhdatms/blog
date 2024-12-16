package com.group7.blog.mappers;

import com.group7.blog.dto.Report.reponse.ReportResponse;
import com.group7.blog.dto.Report.reponse.ReportDetailResponse;
import com.group7.blog.models.Report;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReportMapper {


    @Mapping(target = "blogId", source = "report.blog.id")
    @Mapping(target ="reportType", source = "report.reportType")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "reportStatus", source = "reportStatus")
    @Mapping(target = "id", source = "id")
    @Mapping(target = "createdBy", source = "createdBy")
    @Mapping(target = "userId", source = "report.users.id")
    ReportResponse toResponse(Report report);

    // Map Report entity to ReportDetailResponse
    @Mapping(target = "reportResponse", source = "report")
    @Mapping(target = "user.id", source = "users.id")
    @Mapping(target = "user.username", source = "users.username")
    @Mapping(target = "user.nameTag", source = "users.nameTag")
    @Mapping(target = "user.avatar", source = "users.avatar")
    @Mapping(target = "thumbnail", source = "blog.thumbnail", defaultExpression = "java(null)")
    @Mapping(target = "blogTitle", source = "blog.title", defaultExpression = "java(null)")
    ReportDetailResponse toReportDetailResponse(Report report);

}
