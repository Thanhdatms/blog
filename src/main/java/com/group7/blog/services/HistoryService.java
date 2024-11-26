package com.group7.blog.services;

import com.group7.blog.dto.History.request.HistoryCreation;
import com.group7.blog.mappers.HistoryMapper;
import com.group7.blog.repositories.HistoryRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class HistoryService {
    HistoryRepository historyRepository;
    HistoryMapper historyMapper;

    public void createHistory(HistoryCreation request) {
        historyRepository.save(historyMapper.toHistory(request));
    }
}
