package com.workspace.workspace.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ProjectCreateRequest {

    @NotBlank
    private String projectName;

    @NotBlank
    private String description;

    private LocalDate startDate;

    @NotNull
    private LocalDate dueDate;

}
