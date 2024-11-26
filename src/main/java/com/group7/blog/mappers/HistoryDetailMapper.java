package com.group7.blog.mappers;

import com.group7.blog.dto.History.request.HistoryDetailCreation;
import com.group7.blog.models.HistoryDetail;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)

public interface HistoryDetailMapper {
    HistoryDetail toHistoryDetail(HistoryDetailCreation request);
}
