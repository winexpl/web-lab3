package com.lab2.dto;

import java.time.LocalDate;

import com.lab2.entity.Project;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TaskDTO {
    private int id;
    private int projectId;
    private String name;
    private String descr;
    private boolean completed;
    private LocalDate endDate;
}
