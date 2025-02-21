package com.lab2.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.lab2.dto.TaskDTO;
import com.lab2.entity.Task;
import com.lab2.repository.JpaTaskRepository;

@Service
public class TaskService {
    @Autowired
    private JpaTaskRepository taskRepository;


    public List<TaskDTO> findAllTasksByProjectId(int projectId) {
        return taskRepository.findByProjectId(projectId).stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public TaskDTO save(Task task) {
        try {
            return convertToDTO(taskRepository.save(task));
        } catch(DataIntegrityViolationException e) {
            return null;
        }
    }

    public TaskDTO findTaskByProjectIdAndTaskId(int projectId, int taskId){
        Optional<Task> task = taskRepository.findByIdAndProjectId(taskId, projectId);
        if(task.isPresent()) return convertToDTO(task.get());
        else return null;
    }

    public void deleteByProjectIdAndTaskId(int projectId, int taskId) {
        taskRepository.deleteByIdAndProjectId(taskId, projectId);
    }

    public TaskDTO updateTask(int projectId, int taskId, Task task) {
        Optional<Task> foundTask = taskRepository.findByIdAndProjectId(taskId, projectId);
        if(foundTask.isPresent()) {
            task.setId(taskId);
            task.setProjectId(projectId);
            return convertToDTO(taskRepository.save(task));
        } else return null;
    }

    public void deleteCompleted(int projectId) {
        taskRepository.deleteCompleted(projectId);
    }

    private TaskDTO convertToDTO(Task task) {
        return TaskDTO.builder()
            .id(task.getId())
            .name(task.getName())
            .descr(task.getDescr())
            .projectId(task.getProjectId())
            .endDate(task.getEndDate())
            .completed(task.isCompleted()).build();
    }
}
