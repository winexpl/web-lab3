package com.lab2.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lab2.dto.ActiveTasksCount;
import com.lab2.dto.ProjectDTO;
import com.lab2.entity.Project;
import com.lab2.repository.JpaProjectRepository;
import com.lab2.repository.JpaTaskRepository;

import jakarta.transaction.Transactional;

@Service
public class ProjectService {
    @Autowired
    private JpaProjectRepository projectRepository;

    @Autowired
    private JpaTaskRepository taskRepository;

    public ProjectDTO saveProject(Project project) {
        if(project.getBeginDate() == null) project.setBeginDate(LocalDate.now());
        return convertToDTO(projectRepository.save(project));
    }

    public int updateProject(Project project) {
        return projectRepository.update(project.getName(),
        project.getDescr(), project.getBeginDate(), project.getEndDate(), project.getId());
    }

    @Transactional
    public void removeProject(int id) {
        taskRepository.deleteAllByProjectId(id);
        projectRepository.deleteById(id);
    }

    public ProjectDTO findProjectById(int id) {
        Optional<Project> project = projectRepository.findById(id);
        if(project.isPresent()) return convertToDTO(project.get());
        else return null;
    }

    public List<ProjectDTO> findProjectByRangeOfDates(LocalDate start_date, LocalDate end_date) {
        return projectRepository.findByRangeOfDates(start_date, end_date).stream().map(this::convertToDTO).toList();
    }

    public List<ProjectDTO> findByString(String sample) {
        return projectRepository.findByString(sample).stream().map(this::convertToDTO).toList();
    }

    public List<ProjectDTO> findAll(){
        return projectRepository.findAll().stream().map(this::convertToDTO).toList();
    }

    public Map<Integer, Integer> getActiveTasksCountByProject() {
        return projectRepository.getActiveTasksCountByProject().stream().collect(Collectors.toMap(
        ActiveTasksCount::getProjectId,
        ActiveTasksCount::getActiveTasksCount));
    }

    private ProjectDTO convertToDTO(Project project) {
        return ProjectDTO.builder().id(project.getId())
            .name(project.getName())
            .descr(project.getDescr())
            .beginDate(project.getBeginDate())
            .endDate(project.getEndDate()).build();
    }
}
