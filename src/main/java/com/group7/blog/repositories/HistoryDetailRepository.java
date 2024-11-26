package com.group7.blog.repositories;

import com.group7.blog.models.History;
import com.group7.blog.models.HistoryDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HistoryDetailRepository extends JpaRepository<HistoryDetail, Long> {
}
