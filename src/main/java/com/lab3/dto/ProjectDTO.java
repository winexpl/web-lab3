package com.lab3.dto;

import java.time.LocalDate;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProjectDTO {
    private int id;
    private String name;
    private String descr;
    private LocalDate beginDate;
    private LocalDate endDate;
}
