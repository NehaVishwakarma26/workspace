package com.workspace.workspace.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "teams")
@Data
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Pattern(regexp = "TEAM\\d+$",message = "Team id should start with TEAM.")
    private String teamId;

    @NotBlank
    private String teamName;

    @NotBlank
    private String description;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Department department;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Status status;

    @NotNull
    private LocalDate createdDate;
}
