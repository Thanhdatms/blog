package com.group7.blog.repositories;

import com.group7.blog.models.History;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface HistoryRepository extends JpaRepository<History, Long> {
    @Query("SELECT h FROM History h WHERE h.createdAt BETWEEN :startDate AND :endDate")
    List<History> findAllHistoryFromSevenDaysAgo(LocalDateTime startDate, LocalDateTime endDate);
}
