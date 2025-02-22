package com.lab3.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.lab3.dto.ProjectDTO;
import com.lab3.entity.Project;
import com.lab3.service.ProjectService;

import jakarta.annotation.Nullable;
import jakarta.annotation.security.RolesAllowed;
import lombok.AllArgsConstructor;


import java.net.URI;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;



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
    @RolesAllowed("ROLE_ADMIN")
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
    @RolesAllowed("ROLE_ADMIN")
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
    @Secured("ROLE_ADMIN")
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
