package com.group7.blog.services;

import com.group7.blog.dto.History.request.HistoryCreation;
import com.group7.blog.dto.History.request.HistoryDetailCreation;
import com.group7.blog.dto.History.response.HistoryDetailResponse;
import com.group7.blog.enums.ErrorCode;
import com.group7.blog.exceptions.AppException;
import com.group7.blog.mappers.HistoryDetailMapper;
import com.group7.blog.mappers.HistoryMapper;
import com.group7.blog.models.History;
import com.group7.blog.models.HistoryDetail;
import com.group7.blog.repositories.HistoryDetailRepository;
import com.group7.blog.repositories.HistoryRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class HistoryService {
    HistoryRepository historyRepository;
    HistoryDetailRepository historyDetailRepository;
    HistoryMapper historyMapper;
    HistoryDetailMapper historyDetailMapper;


    public History createHistory(HistoryCreation request) {
        return historyRepository.save(historyMapper.toHistory(request));
    }

    public void createHistoryDetails(List<HistoryDetailCreation> request) {
        request.forEach(item -> {
            historyDetailRepository.save(historyDetailMapper.toHistoryDetail(item));
        });
    }

    public List<HistoryDetailCreation> getNonNullFieldNames(Object entity, History history) {

        List<HistoryDetailCreation> list = new java.util.ArrayList<>(List.of());

        Class<?> clazz = entity.getClass();

        // Iterate over the fields of the entity
        for (Field field : clazz.getDeclaredFields()) {
            // Skip transient or static fields
            if (Modifier.isTransient(field.getModifiers()) ||
                    Modifier.isStatic(field.getModifiers())) {
                continue;
            }

            field.setAccessible(true);  // Ensure we can access private fields

            try {
                // Get the value of the field from the entity
                Object value = field.get(entity);

                // Check if the field value is not null
                if (value != null) {
                    HistoryDetailCreation historyDetailCreation = new HistoryDetailCreation();
                    historyDetailCreation.setFieldName(field.getName());
                    historyDetailCreation.setNewValue((String) value);
                    historyDetailCreation.setHistory(history);
                    list.add(historyDetailCreation);
                }
            } catch (IllegalAccessException e) {
                throw new AppException(ErrorCode.INTERNAL_SERVER_ERROR);
            }
        }
        return list;
    }

    // Cron expression to run every week on Sunday at midnight
    @Scheduled(cron = "0 0 0 * * SUN")
    public void writeHistoryToFile() {
        // Get the current timestamp and format it to append to the file name
        String timestamp = getFormattedTimestamp();

        // Create the file name using the current date and time
        String historyFileName = "history_logs_" + timestamp + ".txt";
        String historyDetailFileName = "history_details_logs_" + timestamp + ".txt";

        // Combine the directory path and file name to get the full path
        // Or use a relative path: "./logs/"
        String FILE_DIRECTORY = "C:\\Users\\LEGION 5\\Downloads\\Documents\\Histories\\";
        String historyFilePath = Paths.get(FILE_DIRECTORY, historyFileName).toString();
        String historyDetailFilePath = Paths.get(FILE_DIRECTORY, historyDetailFileName).toString();

        List<History> histories = getHistoryFromSevenDaysAgo();

        // Create a file object with the generated file path
        File historyFile = new File(historyFilePath);
        File historyDetailFile = new File(historyDetailFilePath);
        try (FileWriter fileWriter = new FileWriter(historyFile, true)) {
            // Append the history entry to the file
            histories.forEach(item -> {
                try {
                    fileWriter.write(item.getId() + "\t" +
                            item.getModel() + "\t" +
                            item.getObjectId() + "\t" +
                            item.getActionStatus() + "\t" +
                            item.getActionType() + "\t" +
                            item.getEmail() + "\t" +
                            item.getUsers() + "\t" +
                            item.getCreatedAt() + "\n");
                } catch (IOException e) {

                    throw new AppException(ErrorCode.INTERNAL_SERVER_ERROR);
                }
            });
        } catch (IOException e) {
            throw new AppException(ErrorCode.INTERNAL_SERVER_ERROR);
        }

        try (FileWriter fileWriter = new FileWriter(historyDetailFile, true)) {
            // Append the history entry to the file
            histories.forEach(item -> {
                item.getHistoryDetails().forEach(obj -> {
                    try {
                        fileWriter.write(obj.getId() + "\t" +
                                obj.getFieldName() + "\t" +
                                obj.getNewValue() + "\t" +
                                obj.getHistory().getId() + "\n");
                    } catch (IOException e) {
                        throw new AppException(ErrorCode.INTERNAL_SERVER_ERROR);
                    }
                });
            });
        } catch (IOException e) {
            throw new AppException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    // Utility method to format the current timestamp (e.g., "yyyy-MM-dd_HH-mm-ss")
    private String getFormattedTimestamp() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
        return LocalDateTime.now().format(formatter);
    }

    public List<History> getHistoryFromSevenDaysAgo() {
        // Calculate the start of 7 days ago
        LocalDateTime sevenDaysAgo = LocalDateTime.now().minusDays(7);

        // The end date is the start of today (midnight), so that we get the entire 24-hour period
        LocalDateTime endOfDaySevenDaysAgo = LocalDateTime.now();

        // Get history records from 7 days ago
        return historyRepository.findAllHistoryFromSevenDaysAgo(sevenDaysAgo, endOfDaySevenDaysAgo);
    }

    public void triggerJobManually() {
        writeHistoryToFile();
    }
}
