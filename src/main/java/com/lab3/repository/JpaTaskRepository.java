package com.lab3.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.lab3.entity.Task;

import jakarta.transaction.Transactional;

public interface JpaTaskRepository extends JpaRepository<Task, Integer> {
    List<Task> findByProjectId(int projectId);

    void deleteByIdAndProjectId(int id, int projectId);

    @Modifying
    @Query(nativeQuery = true, name = "Task.deleteAllByProjectId")
    void deleteAllByProjectId(@Param("projectId") int projectId);

    Optional<Task> findByIdAndProjectId(int id, int projectId);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, name = "Task.deleteCompleted")
    void deleteCompleted(@Param("projectId") int projectId);
}