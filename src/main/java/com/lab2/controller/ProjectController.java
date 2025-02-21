package com.lab2.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.lab2.dto.ActiveTasksCount;
import com.lab2.dto.ProjectDTO;
import com.lab2.entity.Project;
import com.lab2.service.ProjectService;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Builder;

import org.springframework.web.bind.annotation.PutMapping;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;



@RestController
@RequestMapping("/api/projects")
@AllArgsConstructor
public class ProjectController {
    
    private ProjectService projectService;

    // 1) Возвращает все проекты, если search не передан.
    @GetMapping
    public List<ProjectDTO> findByString(@RequestParam @Nullable String search) {
        if(search ==  null) return projectService.findAll();
        else return projectService.findByString(search);
    }

    // 2) Возвращает конкретный проект по id.
    @GetMapping("/{id}")
    public ResponseEntity<ProjectDTO> findById(@PathVariable int id) {
        return ResponseEntity.ofNullable(projectService.findProjectById(id));
    }
    
    // 3) Создаёт новый проект (без задач).
    @PostMapping
    ResponseEntity<ProjectDTO> save(@RequestBody Project project) {
        try {
            ProjectDTO savedProject = projectService.saveProject(project);

            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(savedProject.getId())
                    .toUri();

            return ResponseEntity.created(location).body(savedProject);
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    // 4) Обновляет проект.
    @PutMapping("/{id}")
    public ResponseEntity<Integer> update(@PathVariable int id, @RequestBody Project project) {
        project.setId(id);
        try {
            int numberOfRowsAffected = projectService.updateProject(project);
            if (numberOfRowsAffected == 0) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(0);
            } else {
                return ResponseEntity.ok(numberOfRowsAffected);
            }
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.badRequest().body(0);
        }
    }

    // 5) Удаляет проект.
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remove(@PathVariable int id) {
        projectService.removeProject(id);
        return ResponseEntity.ok().build();
    }

    // 6) Получение количества незавершённых задач в проектах
    @GetMapping("/active")
    public Map<Integer, Integer> getActiveTasksCountByProject() {
        return projectService.getActiveTasksCountByProject();
    }
}
