package com.lab3.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.lab3.dto.ActiveTasksCount;
import com.lab3.entity.Project;

import jakarta.transaction.Transactional;

@Primary
public interface JpaProjectRepository extends JpaRepository<Project, Integer> {
    @Modifying
    @Transactional
    @Query(nativeQuery = true, name = "Project.update")
    int update(@Param("name") String name,
        @Param("descr") String descr,
        @Param("beginDate") LocalDate beginDate,
        @Param("endDate") LocalDate endDate,
        @Param("id") int id);

    @Query(nativeQuery = true, name = "Project.findByRangeOfDates")
    List<Project> findByRangeOfDates(@Param("beginDate") LocalDate beginDate,
                        @Param("endDate") LocalDate endDate);

    @Query(nativeQuery = true, name = "Project.findByString")
    List<Project> findByString(@Param("sample") String sample);

    @Query(nativeQuery = true, name = "Project.getActiveTasksCountByProject")
    List<ActiveTasksCount> getActiveTasksCountByProject();

}
