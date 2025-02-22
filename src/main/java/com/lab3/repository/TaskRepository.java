package com.lab3.repository;

import java.util.List;

import com.lab3.entity.Task;

public interface TaskRepository {
    List<Task> findByPId(int pId);
    List<Task> findByIdAndPId(int id, int pId);
    Task save(Task task);
    int deleteByPIdAndId(int pId, int id);
    int deleteCompleted(int pId);
}