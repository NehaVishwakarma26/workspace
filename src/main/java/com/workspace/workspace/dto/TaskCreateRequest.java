package com.workspace.workspace.dto;

import com.workspace.workspace.model.Employee;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class TaskCreateRequest {

    @NotBlank
    private String taskName;

    @NotBlank
    private String description;

    @NotNull
    private LocalDate dueDate;

    private List<String> employeeIds;
}
