package com.group7.blog.repositories;

import com.group7.blog.enums.EnumData;
import com.group7.blog.models.Report;
import com.group7.blog.models.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Integer> {
    Page<Report> findByUsers(Users user, Pageable pageable);
    Page<Report> findByReportStatus(EnumData.ReportStatus reportStatus, Pageable pageable);
}
