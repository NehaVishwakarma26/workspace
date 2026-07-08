package com.workspace.workspace.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "tasks")
@Data
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Pattern(
            regexp = "^TASK\\d+$",
            message = "Task ID should start with TASK followed by numbers"
    )
    private String taskId;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "project_id",nullable = false)
    private Project project;

    @NotBlank
    private String taskName;

    @NotBlank
    private String description;

    @NotNull
    private LocalDate createdAt;

    @NotNull
    private LocalDate dueDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    private TaskStatus taskStatus;
}
