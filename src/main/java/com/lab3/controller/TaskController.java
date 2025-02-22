package com.lab3.controller;

import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lab3.dto.TaskDTO;
import com.lab3.entity.Task;
import com.lab3.service.TaskService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("api/projects/{projectId}/tasks")
public class TaskController {
    @Autowired
    private TaskService taskService;

    // 1) Возвращает все задачи указанного проекта.
    @GetMapping 
    public List<TaskDTO> findAllTasksByProjectId(@PathVariable int projectId) {
        return taskService.findAllTasksByProjectId(projectId);
    }

    // 2) Возвращает конкретную задачу по id задачи и id проекта.
    @GetMapping("/{taskId}")
    public ResponseEntity<TaskDTO> findTaskByProjectIdAndTaskId(@PathVariable int projectId,
        @PathVariable int taskId) {
        return ResponseEntity.ofNullable(taskService.findTaskByProjectIdAndTaskId(projectId, taskId));
    }

    // 3) Создаёт новую задачу в указанном проекте.
    @PostMapping
    public ResponseEntity<TaskDTO> saveTaskForProject(@PathVariable int projectId, @RequestBody Task task) {
        task.setProjectId(projectId);
        return ResponseEntity.ofNullable(taskService.save(task));
    }
    
    // 4) Обновляет задачу. Новые данные передаются в теле запроса.
    @PutMapping("/{taskId}")
    public ResponseEntity<TaskDTO> updateTask(@PathVariable int projectId, @PathVariable int taskId, @RequestBody Task task) {
        return ResponseEntity.ofNullable(taskService.updateTask(projectId, taskId, task));
    }
    
    // 5) Удаляет задачу по taskId и projectId.
    @DeleteMapping("/{taskId}")
    public void deleteByProjectIdAndTaskId(@PathVariable int projectId,
        @PathVariable int taskId) {
        deleteByProjectIdAndTaskId(projectId, taskId);
    }

    // 6) Удаляет все задачи проекта, у которых completed = true.
    @DeleteMapping
    public void deleteAllCompleted(@PathVariable int projectId) {
        taskService.deleteCompleted(projectId);
    }
    
}

