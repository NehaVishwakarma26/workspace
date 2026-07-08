package com.workspace.workspace.dto;

import com.workspace.workspace.model.TaskStatus;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class TaskDetailsResponse {

    private String taskId;

    private String taskName;

    private String description;

    private LocalDate dueDate;

    private LocalDate createdDate;

    private TaskStatus taskStatus;

    private List<String> assignedEmployee;
}
