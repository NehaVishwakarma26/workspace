package com.workspace.workspace.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "projects")
@Data
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Pattern(regexp = "PROJECT\\d+$",message = "Project name should start with PROJECT")
    private String projectId;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

    @NotBlank
    private String projectName;

    @NotBlank
    private String description;

    private LocalDate startDate;

    @NotNull
    private LocalDate dueDate;

    @NotNull
    private LocalDate createdDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    private ProjectStatus projectStatus;

}
