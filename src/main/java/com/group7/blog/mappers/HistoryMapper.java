package com.group7.blog.mappers;

import com.group7.blog.dto.History.request.HistoryCreation;
import com.group7.blog.models.History;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface HistoryMapper {
    History toHistory(HistoryCreation request);
}
