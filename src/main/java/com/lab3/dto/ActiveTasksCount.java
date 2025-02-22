package com.lab3.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ActiveTasksCount {
    private Integer projectId;
    private Integer activeTasksCount;
}